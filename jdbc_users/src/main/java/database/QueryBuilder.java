package database;

import database.enums.Instances;
import database.sqllanguage.DDL;
import database.sqllanguage.DML;
import org.jetbrains.annotations.NotNull;

import java.util.Formatter;
import java.util.Map;

/**
 * Генератор запросов для метода {@link database.DatabaseController#buildQuery(QueryBuilder)}
 * Реализует {@link database.sqllanguage.DDL} и {@link database.sqllanguage.DML} языковые интерфейсы
 */
public class QueryBuilder implements DDL, DML {

    private StringBuilder QUERY = new StringBuilder();

    /**
     * Выбор конкретных полей для оператора select
     * @param columns названия полей
     * @return this
     */
    @Override
    public QueryBuilder select(String @NotNull ... columns) {
        QUERY.append("SELECT ");
        for (String column : columns) {
            QUERY.append(column).append(", ");
        }
        QUERY.replace(QUERY.length() - 2, QUERY.length() - 1, "\n");
        return this;
    }
    /**
     * Выбор всех полей для оператора select
     * @return this
     */
    @Override
    public QueryBuilder selectAll() {
        QUERY.append("SELECT *\n");
        return this;
    }
    /**
     * Оператор insert
     * @param instance объект, с которым будем взаимодействавать
     * @return this
     */
    @Override
    public QueryBuilder insert(String instance) {
        QUERY.append("INSERT INTO ").append(instance).append("\n");
        return this;
    }
    /**
     * Оператор update
     * @param instance объект, с которым будем взаимодействавать
     * @return this
     */
    public QueryBuilder update(String instance) {
        QUERY.append("UPDATE ").append(instance).append("\n");
        return this;
    }
    /**
     * Оператор delete
     * @param instance объект, с которым будем взаимодействавать
     * @return this
     */
    @Override
    public QueryBuilder delete(String instance) {
        QUERY.append("DELETE FROM ").append(instance).append("\n");
        return this;
    }
    /**
     * Оператор create
     * @param instance объект, который будем создавать
     * @param name имя объекта
     * @return this
     */
    @Override
    public QueryBuilder create(Instances instance, String name) {
        QUERY.append("CREATE ").append(instance).append(" ").append(name).append("\n");
        return this;
    }
    /**
     * Оператор alter
     * @param table имя таблицы
     * @return this
     */
    @Override
    public QueryBuilder alter(String table) {
        QUERY.append("ALTER TABLE ").append(table).append("\n");
        return this;
    }
    /**
     * Оператор drop
     * @param table имя таблицы
     * @return this
     */
    @Override
    public QueryBuilder drop(String table) {
        QUERY.append("DROP TABLE ").append(table).append("\n");
        return this;
    }
    /**
     * Оператор from
     * @param table имя таблицы
     * @return this
     */
    public QueryBuilder from(String table) {
        QUERY.append("FROM ").append(table).append("\n");
        return this;
    }
    /**
     * Оператор where
     * @param condition условие для выборки
     * @return this
     */
    public QueryBuilder where(String condition) {
        QUERY.append("WHERE ").append(condition).append("\n");
        return this;
    }
    /**
     * Оператор set
     * @param values набор пар ключ-значение, находящихся в Map. Должны соответствовать структуре <Поле = Значение>
     * @return this
     */
    public QueryBuilder set(@NotNull Map<String, Object> values) {
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
    /**
     * Оператор set
     * @param field поле из таблицы
     * @param value значение для этого поля
     * @return this
     */
    public QueryBuilder set(String field, Object value) {
        if (value instanceof String) {
            value = String.format("'%s'", value);
        }
        QUERY.append("SET ").append(field).append(" = ").append(value).append("\n");
        return this;
    }
    /**
     * Поля в таблице, с которыми будут производиться манипуляции
     * @param columns названия полей
     * @return this
     */
    public QueryBuilder columns(String @NotNull ... columns) {
        QUERY.append("(");
        for (String column : columns) {
            QUERY.append(column).append(", ");
        }
        QUERY.replace(QUERY.length() - 2, QUERY.length() - 1, ")\n");
        return this;
    }
    /**
     * Значения, которые перечисляются после insert() метода и columns() метода
     * @param values значения
     * @return this
     */
    public QueryBuilder values(Object @NotNull ... values) {
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
    /**
     * Метод для создания запроса из готового запроса
     * @param query готовый запрос без переменных
     * @return this
     */
    public QueryBuilder customQuery(String query) {
        QUERY = new StringBuilder().append(query);
        return this;
    }
    /**
     * Метод для создания запроса с помощью конструктора и переменных.
     * Принимает два параметра на вход:
     * @param queryPattern конструктор запроса с объявленными переменными
     * @param args значения переменных для запроса
     * @return this
     */
    public QueryBuilder customQuery(String queryPattern, Object... args) {
        queryPattern = queryPattern.replace("= %s", "= '%s'")
                .replace("=%s", "='%s'");
        QUERY = new StringBuilder().append(new Formatter().format(queryPattern, args));
        return this;
    }
    /**
     * Печать запроса в консоль
     * @return this
     */
    public QueryBuilder printQuery() {
        System.out.println(QUERY);
        return this;
    }
    /**
     * Возвращает запрос в строковом формате. Используется в {@link database.DatabaseController#buildQuery(QueryBuilder)}
     * @return String
     */
    public String getFinalQuery() {
        return QUERY.toString();
    }
    /**
     * Сбрасывает билдер с запросом
     */
    public void clearQuery() {
        QUERY = new StringBuilder();
    }
}