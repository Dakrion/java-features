import database.DatabaseController;
import database.QueryBuilder;
import io.qameta.allure.Feature;
import model.Response;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static database.interfaces.SingleConnection.closeConnection;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Feature("Тесты с single connection")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DbSingleTests {

    private static final String SELECT_ALL_COLUMNS = "SELECT * FROM users WHERE %s = %s";

    private static final String INSERT_SQL_USERS = "INSERT INTO users (%s,%s,%s,%s,%s)" +
            " values (\"%s\",\"%s\",\"%s\",\"%s\",%d)";

    private DatabaseController databaseController;

    private QueryBuilder queryBuilder;

    private List<Response> list = new ArrayList<>();

    private Response response;

    @BeforeAll
    void init() {
        databaseController = new DatabaseController();
        queryBuilder = new QueryBuilder();
    }

    @AfterAll
    void stop() throws SQLException {
        closeConnection();
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

        step("Сделать запрос в бд для получения одной записи с одним полем", () -> {
            response = databaseController
                    .buildQuery(queryBuilder
                            .select("nickname")
                            .from("users")
                            .where("id = 1")
                            .printQuery())
                    .execute()
                    .printResult()
                    .extractAs(Response.class);
        });

        step("Проверить результат", () -> assertSoftly(softly -> assertThat(response.getNickname()).isEqualTo("dwana.turcotte")));
    }

    @Test
    @DisplayName("Тест на проверку update-запроса")
    void updateTests() {
        String email = "test123@mail.ru";

        step("Сделать запрос в бд для обновления записи", () -> {

            databaseController
                    .buildQuery(queryBuilder
                            .update("users")
                            .set("email", email)
                            .where("id = 3")
                            .printQuery())
                    .execute();
        });

        step("Проверить результат", () -> {
            response = databaseController
                    .buildQuery(queryBuilder
                            .select("email")
                            .from("users")
                            .where("id = 3")
                            .printQuery())
                    .execute()
                    .printResult()
                    .extractAs(Response.class);
            assertThat(response.getEmail()).isEqualTo(email);
        });

        step("Изменить email для последующих тестов", () -> {
            databaseController
                    .buildQuery(queryBuilder
                            .update("users")
                            .set("email", "empty")
                            .where("id = 3")
                            .printQuery())
                    .execute();
        });
    }

    @Test
    @DisplayName("Тест на проверку insert-запроса с использованием SingleConnection")
    void insertTests() {
        step("Сделать запрос в бд для создания записи", () -> {

            databaseController
                    .buildQuery(queryBuilder
                            .insert("users")
                            .columns("nickname", "email", "password")
                            .values("Dominik", "Dom@email.ru", "12345")
                            .printQuery())
                    .execute();
        });

        step("Сделать запрос в бд для получения созданной записи", () -> {
            response = databaseController
                    .buildQuery(queryBuilder
                            .selectAll()
                            .from("users")
                            .where("nickname = 'Dominik'")
                            .printQuery())
                    .execute()
                    .printResult()
                    .extractAs(Response.class);
        });

        step("Проверить результат", () -> assertSoftly(softly -> {
            assertThat(response.getEmail()).isEqualTo("Dom@email.ru");
            assertThat(response.getPassword()).isEqualTo("12345");
            assertThat(response.getNickname()).isEqualTo("Dominik");
        }));

        step("Удалить созданную запись", () -> {
            databaseController
                    .buildQuery(queryBuilder
                            .delete("users")
                            .where("id = " + response.getId())
                            .printQuery())
                    .execute()
                    .printResult();
        });
    }
}