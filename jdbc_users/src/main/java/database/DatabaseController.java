package database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import database.exceptions.ConvertResultException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Контроллер для базы данных
 */
@Slf4j
public class DatabaseController {

    private String query;

    private ResultSet rs;

    private List<Map<String, Object>> resultList;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * Метод, возвращающий готовый запрос из {@link QueryBuilder}, а затем очищает запрос после получения
     *
     * @param builder
     * @return this
     */
    public DatabaseController buildQuery(@NotNull QueryBuilder builder) {
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
    public DatabaseController execute() {
        try (Connection connection = DBConnection.openConnection()) {
            try {
                assert connection != null;
                try (Statement statement = connection.createStatement()) {
                    connection.setAutoCommit(true);
                    if (query.startsWith("SELECT ") || query.startsWith("select ")) {
                        rs = statement.executeQuery(query);
                        resultList = saveResult();
                    } else {
                        statement.executeUpdate(query);
                        System.out.println(statement.getUpdateCount() + " rows affected");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    rs.close();
                }
            } catch (Exception e) {
                throw new NullPointerException("Connection is null!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Выводит результат запроса на консоль
     *
     * @return this
     */
    public DatabaseController printResult() {
        if (resultList != null && !resultList.isEmpty()) {
            String formatResult = resultList.toString().replace("[{", "")
                    .replace("}]", "")
                    .replace("}, {", "\n\n")
                    .replace("=", " : ")
                    .replace(", ", "\n");
            System.out.println(formatResult);
        } else {
            System.out.println("No results for this query");
        }
        return this;
    }

    /**
     * Преобразует результат запроса в объект класса {@link Class<>T</>}
     *
     * @return {@link class T}
     * @throws ConvertResultException
     */
    public <T> T extractAs(Class<T> cls) {
        if (resultList.size() == 1) {
            return mapper.convertValue(resultList.get(0), cls);
        } else
            throw new ConvertResultException("Результат запроса более 1 записи, используйте метод extractAsList() для извлечения");
    }

    /**
     * Преобразует результат запроса в список объектов класса {@link Class<>T</>}
     *
     * @return {@link List<>T</>
     */
    public <T> List<T> extractAsList(Class<T> cls) {
        List<T> result = new ArrayList<>();
        for (Map<String, Object> obj : resultList) {
            T value = mapper.convertValue(obj, cls);
            result.add(value);
        }
        return result;
    }

    /**
     * Сохраняет результат запроса из {@link java.sql.ResultSet}
     *
     * @return List
     * @throws SQLException
     */
    private @NotNull List<Map<String, Object>> saveResult() throws SQLException {
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
}