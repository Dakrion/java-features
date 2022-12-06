package database.sqllanguage;

import database.enums.Instances;
import database.QueryBuilder;

/**
 * Data Definition Language интерфейс
 */
public interface DDL {

    QueryBuilder create(Instances instance, String name);

    QueryBuilder alter(Instances instance, String name);

    QueryBuilder drop(Instances instance, String name);

    QueryBuilder truncate(String table);
}