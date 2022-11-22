package database;

import config.BaseConfig;
import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Класс с конфигурацией базы данных и методами для установки соединения
 */
@Slf4j
public class DBConnection {

    private DBConnection() {
        throw new IllegalStateException("Utility class");
    }

    private static final BaseConfig config = ConfigFactory.newInstance()
            .create(BaseConfig.class);

    private static final Properties props = new Properties();

    private static final String URL;

    private static Connection connection;

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
    protected static Connection openConnection() {
        try {
            closeConnection();
            connection = DriverManager.getConnection(URL, props);
            return connection;
        } catch (SQLException e) {
            log.error(ERROR_GET_CONNECTION, e);
        }
        throw new NullPointerException("Соединение с базой данных == null");
    }

    /**
     * Метод для закрытия соединения с базой данных
     *
     * @throws SQLException
     */
    public static void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}