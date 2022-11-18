package database.sqllanguage;

import database.QueryBuilder;

/**
 * Data Manipulation Language интерфейс
 */
public interface DML {

    QueryBuilder select(String... columns);

    QueryBuilder selectAll();

    QueryBuilder insert(String instance);

    QueryBuilder update(String instance);

    QueryBuilder delete(String instance);
}