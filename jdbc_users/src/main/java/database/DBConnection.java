package database;

import config.BaseConfig;
import database.interfaces.ConnectionPool;
import database.interfaces.SingleConnection;
import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Класс с конфигурацией базы данных и методами для установки соединения
 */
@Slf4j
public class DBConnection implements SingleConnection, ConnectionPool {

    private DBConnection() {
        throw new IllegalStateException("Utility class");
    }

    private static final BaseConfig config = ConfigFactory.newInstance()
            .create(BaseConfig.class);

    private static final Properties props = new Properties();

    private static final String URL;

    private static Connection connection;

    private static List<Connection> connectionPool;

    private static final List<Connection> usedConnections = new ArrayList<>();

    private static final int MAX_POOL_SIZE = 10;

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
     * @throws NullPointerException - при неудачной попытке соединения
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
     * @throws SQLException
     */
    public static void createPool() throws SQLException {
        if (connectionPool == null) {
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
    public static Connection getConnection() throws SQLException {
        if (connectionPool.isEmpty()) {
            if (usedConnections.size() < MAX_POOL_SIZE) {
                connectionPool.add(DriverManager.getConnection(URL, props));
            } else {
                throw new RuntimeException(
                        "Maximum pool size reached, no available connections!");
            }
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
    public static boolean releaseConnection(Connection connection) {
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
            usedConnections.forEach(DBConnection::releaseConnection);

            for (Connection c : connectionPool) {
                c.close();
            }
            connectionPool.clear();
        } else System.out.println("Connection pool was not used");
    }
}