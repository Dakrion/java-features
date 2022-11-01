package utils;

import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.SoftAssertionsProvider;

import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Contains rest assured response and most needed methods
 */
@RequiredArgsConstructor
public class ResponseWrapper {

    private final Response response;

    public <T> T as(Class<T> cls) {
        return response.as(cls);
    }

    public <T> List<T> asList(Class<T> cls) {
        return List.of(response.as(cls));
    }

    public int getStatusCode() {
        return response.getStatusCode();
    }

    public ResponseWrapper assertStatusCode(int expectedStatusCode) {
        assertThat(getStatusCode()).isEqualTo(expectedStatusCode);
        return this;
    }

    public ResponseWrapper printResponseToConsole() {
        response.prettyPrint();
        return this;
    }

    public static void assertSoftly(Consumer<SoftAssertions> softly) {
        SoftAssertionsProvider.assertSoftly(SoftAssertions.class, softly);
    }
}
