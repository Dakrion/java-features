package database.interfaces;

import database.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Интерфейс для взаимодействия с пулом активных соединений с базой данных
 */
public interface ConnectionPool {

    /**
     * Создает пул соединений
     * @param poolSize размер пула
     */
    static void createPool(Integer poolSize) throws SQLException {
        DBConnection.createPool(poolSize);
    }

    /**
     * Берет соединение из пула
     *
     * @return {@link Connection}
     */
    static Connection getConnectionFromPool() throws SQLException {
        return DBConnection.getConnectionFromPool();
    }

    /**
     * Возвращает соединение в пул
     *
     * @param connection возвращаемое соединение
     * @return true - если успешно возвращено и убрано из использованных
     */
    static boolean releaseConnection(Connection connection) {
        return DBConnection.releaseConnection(connection);
    }

    /**
     * Закрывает все активные соединения и очищает пул
     * @throws SQLException
     */
    static void shutDown() throws SQLException {
        DBConnection.shutDown();
    }
}
