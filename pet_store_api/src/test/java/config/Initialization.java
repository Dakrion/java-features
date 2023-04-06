package config;

import com.google.common.collect.ImmutableMap;
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
import utils.annotations.AnnotationProcessor;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;
import static utils.annotations.AnnotationProcessor.getServiceBaseUrl;

/**
 * Base setup before run tests
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Initialization {

    protected PetApi petApi;

    /**
     * Setup execute before execute test-suites.
     * Contains allureEnvironmentWriter for add environment to allure report, requestSpecification and service api realization
     */
    @BeforeAll
    public void setup() {
        allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("Version api", "v2")
                        .put("Stand", "Stage")
                        .put("URL", getServiceBaseUrl(PetApi.class))
                        .build(),
                        "build/allure-results/");

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        final RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBaseUri(getServiceBaseUrl(PetApi.class))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();

        petApi = new PetClient(requestSpecification);
    }
}
