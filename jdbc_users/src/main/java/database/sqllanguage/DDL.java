package database.sqllanguage;

import database.enums.Instances;
import database.QueryBuilder;

/**
 * Data Definition Language интерфейс
 */
public interface DDL {

    QueryBuilder create(Instances instance, String name);

    QueryBuilder alter(String table);

    QueryBuilder drop(String table);
}