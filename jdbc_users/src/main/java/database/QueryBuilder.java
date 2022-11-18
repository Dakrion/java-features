package database;

import database.enums.Instances;
import database.sqllanguage.DDL;
import database.sqllanguage.DML;

import java.util.Formatter;
import java.util.Map;

/**
 * Генератор запросов для метода {@link database.DatabaseController#buildQuery(QueryBuilder)}
 * Реализует {@link database.sqllanguage.DDL} и {@link database.sqllanguage.DML} языковые интерфейсы
 */
public class QueryBuilder implements DDL, DML {

    private StringBuilder QUERY = new StringBuilder();

    public QueryBuilder select(String... columns) {
        QUERY.append("SELECT ");
        for (String column : columns) {
            QUERY.append(column).append(", ");
        }
        QUERY.replace(QUERY.length() - 2, QUERY.length() - 1, "\n");
        return this;
    }

    @Override
    public QueryBuilder selectAll() {
        QUERY.append("SELECT *\n");
        return this;
    }

    @Override
    public QueryBuilder insert(String instance) {
        QUERY.append("INSERT INTO ").append(instance).append("\n");
        return this;
    }

    public QueryBuilder update(String instance) {
        QUERY.append("UPDATE ").append(instance).append("\n");
        return this;
    }

    @Override
    public QueryBuilder delete(String instance) {
        QUERY.append("DELETE FROM ").append(instance).append("\n");
        return this;
    }

    @Override
    public QueryBuilder create(Instances instance, String name) {
        QUERY.append("CREATE ").append(instance).append(" ").append(name).append("\n");
        return this;
    }

    @Override
    public QueryBuilder alter(String table) {
        QUERY.append("ALTER TABLE ").append(table).append("\n");
        return this;
    }

    @Override
    public QueryBuilder drop(String table) {
        QUERY.append("DROP TABLE ").append(table).append("\n");
        return this;
    }

    public QueryBuilder from(String table) {
        QUERY.append("FROM ").append(table).append("\n");
        return this;
    }

    public QueryBuilder where(String condition) {
        QUERY.append("WHERE ").append(condition).append("\n");
        return this;
    }

    public QueryBuilder set(Map<String, Object> values) {
        values
                .entrySet()
                .forEach(el -> {
                    if (el.getValue() instanceof String) {
                        el.setValue(String.format("'%s'", el.getValue()));
                    }
                });
        String queryStr = values.toString()
                .replace("{", "")
                .replace("}", "")
                .replace("=", " = ")
                .replace(", ", ",\n    ");
        QUERY.append("SET ").append(queryStr).append("\n");
        return this;
    }

    public QueryBuilder set(String field, Object value) {
        if (value instanceof String) {
            value = String.format("'%s'", value);
        }
        QUERY.append("SET ").append(field).append(" = ").append(value).append("\n");
        return this;
    }

    public QueryBuilder columns(String... columns) {
        QUERY.append("(");
        for (String column : columns) {
            QUERY.append(column).append(", ");
        }
        QUERY.replace(QUERY.length() - 2, QUERY.length() - 1, ")\n");
        return this;
    }

    public QueryBuilder values(Object... values) {
        QUERY.append("VALUES (");
        for (Object value : values) {
            if (value instanceof String) {
                value = String.format("'%s'", value);
            }
            QUERY.append(value).append(", ");
        }
        QUERY.replace(QUERY.length() - 2, QUERY.length() - 1, ")\n");
        return this;
    }

    public QueryBuilder customQuery(String query) {
        QUERY = new StringBuilder().append(query);
        return this;
    }

    public QueryBuilder customQuery(String queryPattern, Object... args) {
        queryPattern = queryPattern.replace("= %s", "= '%s'")
                .replace("=%s", "='%s'");
        QUERY = new StringBuilder().append(new Formatter().format(queryPattern, args));
        return this;
    }

    public QueryBuilder printQuery() {
        System.out.println(QUERY);
        return this;
    }

    public String getFinalQuery() {
        return QUERY.toString();
    }

    public void clearQuery() {
        QUERY = new StringBuilder();
    }
}