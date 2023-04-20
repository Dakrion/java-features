package database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import database.exceptions.ConvertResultException;
import database.extensions.logging.AllureAppender;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static database.interfaces.ConnectionPool.*;
import static database.interfaces.SingleConnection.openConnection;

/**
 * Контроллер для базы данных
 */
@Slf4j
public class DatabaseController {

    private String query;

    private ResultSet rs;

    private List<Map<String, Object>> resultList;

    private boolean useConnectionPool = false;

    private boolean attachQueryToAllure = false;

    private AllureAppender appender;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * Метод возвращает готовый запрос из {@link QueryBuilder}, а затем очищает переменную с запросом после получения
     *
     * @param builder объект класса {@link QueryBuilder}, с помощью которого собирается запрос
     * @return this
     */
    public synchronized DatabaseController buildQuery(@NotNull QueryBuilder builder) {
        query = builder.getFinalQuery();
        builder.clearQuery();
        return this;
    }

    /**
     * Метод для отправки запроса. Создает соединение, затем стейтмент и выполняет запрос
     * в зависимости от его содержания
     *
     * @return this
     */
    public synchronized DatabaseController execute() {
        if (attachQueryToAllure) appender.log("Запрос", query);

        try {
            Connection connection = useConnectionPool ? getConnectionFromPool() : openConnection();
            try (Statement statement = connection.createStatement()) {
                connection.setAutoCommit(true);

                if (query.toUpperCase().startsWith("SELECT ")) {
                    rs = statement.executeQuery(query);
                    resultList = saveResult();
                    if (attachQueryToAllure) appender.log("Результат запроса", logFormat());
                } else {
                    resultList = null;
                    statement.executeUpdate(query);
                    System.out.println(statement.getUpdateCount() + " rows affected\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (rs != null) rs.close();
                if (useConnectionPool) releaseConnection(connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Включает использование ConnectionPool вместо одиночного соединения
     *
     * @return this
     */
    public DatabaseController useConnectionPool(Integer poolSize) {
        try {
            createPool(poolSize);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        useConnectionPool = true;

        return this;
    }

    /**
     * Включает функцию с прикреплением аттача с запросом в Allure-отчет
     *
     * @param enabled флаг включения
     * @return this
     */
    public DatabaseController enableLog(boolean enabled) {
        attachQueryToAllure = enabled;
        if (enabled) appender = new AllureAppender();
        return this;
    }

    /**
     * Выводит результат запроса на консоль
     *
     * @return this
     */
    public DatabaseController printResult() {
        System.out.println(logFormat());
        return this;
    }

    /**
     * Преобразует результат запроса в объект класса {@link Class<>T</>}
     *
     * @return {@link class T}
     * @throws ConvertResultException в случае если результат содержит более 1 записи
     */
    public synchronized <T> T extractAs(Class<T> cls) {
        if (resultList.size() == 1) {
            return mapper.convertValue(resultList.get(0), cls);
        } else if (resultList.size() == 0)
            throw new ConvertResultException(
                    "Результат запроса 0 записей");
        else
            throw new ConvertResultException(
                    "Результат запроса более 1 записи, используйте метод extractAsList() для извлечения");
    }

    /**
     * Преобразует результат запроса в список объектов класса {@link Class<>T</>}
     *
     * @return {@link List<>T</>
     */
    public synchronized <T> List<T> extractAsList(Class<T> cls) {
        List<T> result = new ArrayList<>();
        if (resultList.size() > 1) {
            for (Map<String, Object> obj : resultList) {
                T value = mapper.convertValue(obj, cls);
                result.add(value);
            }
            return result;
        } else if (resultList.size() == 0) throw new ConvertResultException(
                "Результат запроса 0 записей");
        else
            throw new ConvertResultException(
                    "Результат запроса 1 запись, используйте метод extractAs() для извлечения");
    }

    /**
     * Сохраняет результат запроса из {@link java.sql.ResultSet}
     *
     * @return List
     * @throws SQLException
     */
    private synchronized List<Map<String, Object>> saveResult() throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        if (rs != null) {
            ResultSetMetaData data = rs.getMetaData();

            while (rs.next()) {
                Map<String, Object> resMap = new HashMap<>();
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    resMap.put(data.getColumnName(i), rs.getObject(i));
                }
                result.add(resMap);
            }
            return result;
        }
        throw new NullPointerException("ResultSet is null!");
    }

    /**
     * Преобразует результат запроса в читабельный вид
     *
     * @return String
     */
    public String logFormat() {
        if (resultList != null && !resultList.isEmpty()) {
            return resultList.toString().replace("[{", "")
                    .replace("}]", "")
                    .replace("}, {", "\n\n")
                    .replace("=", " : ")
                    .replace(", ", "\n");
        } else {
            return "No results for this query";
        }
    }
}