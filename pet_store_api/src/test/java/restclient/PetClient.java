package restclient;

import exceptions.AnnotationException;
import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import utils.ResponseWrapper;
import utils.annotations.restspec.GET;
import utils.annotations.restspec.POST;

import java.lang.reflect.Method;

import static io.restassured.RestAssured.given;

@RequiredArgsConstructor
public class PetClient implements PetApi {

    private final RequestSpecification requestSpecification;

    private final Class<PetApi> api = PetApi.class;

    @Step("Get pet by Id")
    public ResponseWrapper getPet(Integer petId) {
        try {
            Method thisMethod = api.getDeclaredMethod("getPet", Integer.class);
            if (thisMethod.isAnnotationPresent(GET.class)) {
                return new ResponseWrapper(given(requestSpecification)
                        .when()
                        .get(thisMethod.getAnnotation(GET.class).endpoint(), petId)
                        .thenReturn());
            } else throw new AnnotationException("No annotation for this method!");
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
        throw new RuntimeException("Failed to execute this method!");
    }

    @Step("Add pet to store")
    public <T> ResponseWrapper createPet(T body) {
        try {
            Method thisMethod = api.getDeclaredMethod("createPet", Object.class);
            if (thisMethod.isAnnotationPresent(POST.class)) {
                return new ResponseWrapper(given(requestSpecification)
                        .body(body)
                        .when()
                        .post(thisMethod.getAnnotation(POST.class).endpoint())
                        .thenReturn());
            } else throw new AnnotationException("No annotation for this method!");
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
        throw new RuntimeException("Failed to execute this method!");
    }
}
