package restclient;

import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import utils.ResponseWrapper;

import static io.restassured.RestAssured.given;
import static restclient.Endpoints.GET_PET_PATH;
import static restclient.Endpoints.PET_PATH;

@RequiredArgsConstructor
public class PetClient implements PetApi {

    private final RequestSpecification requestSpecification;

    @Step("Get pet by Id")
    public ResponseWrapper getPet(Integer petId) {
        return new ResponseWrapper(given(requestSpecification)
                .when()
                .get(GET_PET_PATH, petId)
                .thenReturn());
    }

    @Step("Add pet to store")
    public <T> ResponseWrapper createPet(T body) {
        return new ResponseWrapper(given(requestSpecification)
                .body(body)
                .when()
                .post(PET_PATH)
                .thenReturn());
    }
}
