package database.interfaces;

import database.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Интерфейс для взаимодействия с одним активным соединением базы данных
 */
public interface SingleConnection {

    /**
     * Открывает соединение с базой данных
     *
     * @return {@link Connection}
     */
    static Connection openConnection() {
        return DBConnection.openConnection();
    }

    /**
     * Закрывает соединение с базой данных
     */
    static void closeConnection() throws SQLException {
        DBConnection.closeConnection();
    }
}
