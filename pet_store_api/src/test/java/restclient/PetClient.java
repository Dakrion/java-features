package restclient;

import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import utils.ResponseWrapper;
import utils.annotations.restspec.DELETE;
import utils.annotations.restspec.GET;
import utils.annotations.restspec.POST;

import static io.restassured.RestAssured.given;
import static utils.annotations.AnnotationProcessor.validateMethod;

/**
 * Service api realization
 */
@RequiredArgsConstructor
public class PetClient implements PetApi {

    private final RequestSpecification requestSpecification;

    private final Class<PetApi> api = PetApi.class;

    @Step("Get pet by Id")
    @SneakyThrows
    public ResponseWrapper getPet(Long petId) {
            return new ResponseWrapper(given(requestSpecification)
                    .when()
                    .get(validateMethod("getPet", api, GET.class).endpoint(), petId)
                    .thenReturn());
    }

    @Step("Add pet to store")
    @SneakyThrows
    public <T> ResponseWrapper createPet(T body) {
            return new ResponseWrapper(given(requestSpecification)
                    .body(body)
                    .when()
                    .post(validateMethod("createPet", api, POST.class).endpoint())
                    .thenReturn());
    }

    @Step("Delete pet from store")
    @SneakyThrows
    public ResponseWrapper deletePet(Long petId) {
            return new ResponseWrapper(given(requestSpecification)
                    .when()
                    .delete(validateMethod("deletePet", api, DELETE.class).endpoint(), petId)
                    .thenReturn());
    }
}
