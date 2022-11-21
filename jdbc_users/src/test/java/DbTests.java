import database.DBConnection;
import database.DatabaseController;
import database.QueryBuilder;
import model.Response;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DbTests {

    private static final String SELECT_ALL_COLUMNS = "SELECT * FROM users WHERE %s = %s";

    private static final String INSERT_SQL_USERS = "INSERT INTO users (%s,%s,%s,%s,%s)" +
            " values (\"%s\",\"%s\",\"%s\",\"%s\",%d)";

    DatabaseController databaseController;

    QueryBuilder queryBuilder;

    List<Response> list = new ArrayList<>();

    Response response;

    @BeforeAll
    void init() {
        databaseController = new DatabaseController();
        queryBuilder = new QueryBuilder();
    }

    @AfterAll
    void stop() throws SQLException {
        DBConnection.closeConnection();
    }

    @Test
    @DisplayName("Тест на проверку select-запроса")
    void selectTests() {
        step("Сделать запрос в бд для получения всех записей юзеров", () -> {
            list = databaseController
                    .buildQuery(queryBuilder
                            .selectAll()
                            .from("users")
                            .printQuery())
                    .execute()
                    .printResult()
                    .extractAsList(Response.class);
        });

        step("Проверить что результат не пустой", () -> assertThat(list).isNotNull());

        step("Сделать запрос в бд для получения одной записи", () -> {
            response = databaseController
                    .buildQuery(queryBuilder
                            .selectAll()
                            .from("users")
                            .where("id = 1")
                            .printQuery())
                    .execute()
                    .printResult()
                    .extractAs(Response.class);
        });

        step("Проверить результат", () -> assertSoftly(softly -> {
            assertThat(response.getId()).isEqualTo(1);
            assertThat(response.is_male()).isEqualTo(false);
            assertThat(response.getNickname()).isEqualTo("dwana.turcotte");
        }));
    }
}