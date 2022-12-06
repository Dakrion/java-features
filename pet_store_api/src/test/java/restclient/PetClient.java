package restclient;

import exceptions.AnnotationException;
import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import utils.ResponseWrapper;
import utils.annotations.restspec.DELETE;
import utils.annotations.restspec.GET;
import utils.annotations.restspec.POST;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static io.restassured.RestAssured.given;

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
                    .get(validateAnnotation("getPet", api, GET.class, Long.class).endpoint(), petId)
                    .thenReturn());
    }

    @Step("Add pet to store")
    @SneakyThrows
    public <T> ResponseWrapper createPet(T body) {
            return new ResponseWrapper(given(requestSpecification)
                    .body(body)
                    .when()
                    .post(validateAnnotation("createPet", api, POST.class, Object.class).endpoint())
                    .thenReturn());
    }

    @Step("Delete pet from store")
    @SneakyThrows
    public ResponseWrapper deletePet(Long petId) {
            return new ResponseWrapper(given(requestSpecification)
                    .when()
                    .delete(validateAnnotation("deletePet", api, DELETE.class, Long.class).endpoint(), petId)
                    .thenReturn());
    }

    /**
     * Method, which check the annotation for method in interface
     *
     * @param methodName      - name of method
     * @param annotationClass - annotation for this method
     * @param parameterTypes  - parameter types for method
     * @return {@link Annotation}
     */
    public static <T extends Annotation> T validateAnnotation(final String methodName, final Class<?> feature,
                                                              final Class<T> annotationClass,
                                                              final Class<?>... parameterTypes) {
        try {
            Method thisMethod = feature.getDeclaredMethod(methodName, parameterTypes);
            if (thisMethod.isAnnotationPresent(annotationClass)) {
                return thisMethod.getAnnotation(annotationClass);
            }
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
        throw new AnnotationException("No annotation for this method!");
    }
}
