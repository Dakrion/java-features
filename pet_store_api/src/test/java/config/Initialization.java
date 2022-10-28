package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import restclient.PetApi;
import restclient.PetClient;

/**
 * Base setup before run tests
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Initialization {

    private final BaseConfig config = ConfigFactory.create(BaseConfig.class, System.getenv());

    protected PetApi petApi;

    @BeforeAll
    public void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        final RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBaseUri(config.petBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();

        petApi = new PetClient(requestSpecification);
    }
}
