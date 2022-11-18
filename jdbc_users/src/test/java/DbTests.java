import database.DBConnection;
import database.DatabaseController;
import database.QueryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DbTests {

    private static final String SELECT_ALL_COLUMNS = "SELECT * FROM users WHERE %s = %s";

    private static final String INSERT_SQL_USERS = "INSERT INTO users (%s,%s,%s,%s,%s)" +
            " values (\"%s\",\"%s\",\"%s\",\"%s\",%d)";

    DatabaseController databaseController;

    QueryBuilder queryBuilder;

    @BeforeAll
    void init() {
        databaseController = new DatabaseController();
        queryBuilder = new QueryBuilder();
    }

    @AfterEach
    void stop() throws SQLException {
        DBConnection.closeConnection();
    }

    @Test
    void example() {
        Map<Object, Object> map = new HashMap<>();
        map.put("nickname", "Robert");
        map.put("email", "test12@mail.ru");

//        databaseController
//
//                .buildQuery(queryBuilder
//                        .select("id")
//                        .from("users")
//                        .where("id = 1")
//                        .printQuery())
//                .execute()
//                .printResult()
//                .extractToMap();
//
//        databaseController
//
//                .buildQuery(queryBuilder
//                        .selectAll()
//                        .from("users"))
//                .execute()
//                .printResult();

//        databaseController
//                .buildQuery(queryBuilder
//                        .customQuery(INSERT_SQL_USERS, "nickname", "email", "password", "birthdate", "is_male", "Tom", "tom@mail.ru", "fgflk", LocalDate.now().toString(), 1)
//                        .printQuery());
//                .execute()
//                .printResult();
        databaseController
                .buildQuery(queryBuilder
                        .insert("users")
                        .columns("nickname", "email", "password", "birthdate", "is_male")
                        .values("Malek", "ml@mail.ru", "fgflk", LocalDate.now().toString(), 1)
                        .printQuery())
                .execute()
                .printResult();

//        List<Response> list = databaseController
//                .buildQuery(queryBuilder
//                        .selectAll()
//                        .from("users"))
//                .execute()
//                .extractAsList(Response.class);
//
//        list.forEach(System.out::println);
    }
}
