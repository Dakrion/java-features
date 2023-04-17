package database;

import config.BaseConfig;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Класс с конфигурацией базы данных и методами для установки соединения
 */
@Slf4j
@UtilityClass
public class DBConnection {

    private static final BaseConfig config = ConfigFactory.newInstance()
            .create(BaseConfig.class);

    private static final Properties props = new Properties();

    private static final String URL;

    private static Connection connection;

    private static List<Connection> connectionPool;

    private static final List<Connection> usedConnections = new CopyOnWriteArrayList<>();

    private static Integer MAX_POOL_SIZE;

    private static final int MAX_TIMEOUT = 3000;

    private static final String ERROR_GET_CONNECTION = "Неуспешное подключение к базе данных";

    static {
        props.setProperty("user", config.databaseUser());
        props.setProperty("password", config.databasePassword());
        props.setProperty("ssl", "false");
        URL = config.databaseUrl();
    }

    /**
     * Открывает соединение с базой данных. Перед этим закрывает старое соединение если такое есть
     *
     * @return {@link java.sql.Connection}
     * @throws NullPointerException  при неудачной попытке соединения
     */
    public static Connection openConnection() {
        try {
            closeConnection();
            connection = DriverManager.getConnection(URL, props);
            return connection;
        } catch (SQLException e) {
            log.error(ERROR_GET_CONNECTION, e);
        }
        throw new NullPointerException(
                "Соединение с базой данных == null");
    }

    /**
     * Метод для закрытия соединения с базой данных
     */
    public static void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * Создает пул соединений к базе данных
     * @param poolSize размер пула
     * @throws SQLException
     */
    public static void createPool(Integer poolSize) throws SQLException {
        if (connectionPool == null) {
            MAX_POOL_SIZE = poolSize;
            connectionPool = new ArrayList<>(MAX_POOL_SIZE);

            for (int i = 0; i < MAX_POOL_SIZE; i++) {
                connectionPool.add(DriverManager.getConnection(URL, props));
            }
        }
    }

    /**
     * Берет соединение из пула
     *
     * @return {@link Connection}
     * @throws SQLException
     */
    public static synchronized Connection getConnectionFromPool() throws SQLException {
        if (connectionPool.isEmpty()) {
            if (usedConnections.size() < MAX_POOL_SIZE) {
                connectionPool.add(DriverManager.getConnection(URL, props));
            } else
                throw new RuntimeException(
                        "Maximum pool size reached, no available connections!");
        }

        Connection connection = connectionPool
                .remove(connectionPool.size() - 1);

        if (!connection.isValid(MAX_TIMEOUT)) {
            connection = DriverManager.getConnection(URL, props);
        }
        usedConnections.add(connection);

        return connection;
    }

    /**
     * Возвращает соединение в пул после использования
     *
     * @param connection соединение для возврата
     * @return boolean
     */
    public static synchronized boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);

        return usedConnections.remove(connection);
    }

    /**
     * Закрывает все активные соединения в пуле и очищает его
     *
     * @throws SQLException
     */
    public static void shutDown() throws SQLException {
        if (connectionPool != null) {
            if (!usedConnections.isEmpty()) usedConnections.forEach(DBConnection::releaseConnection);

            if (!connectionPool.isEmpty()) {
                for (Connection c : connectionPool) {
                    c.close();
                }
                connectionPool.clear();
            }
        }
    }
}