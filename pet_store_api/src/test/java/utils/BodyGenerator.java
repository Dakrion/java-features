package utils;

import com.github.javafaker.Faker;
import dto.request.PetModel;
import lombok.experimental.UtilityClass;

/**
 * Class to generate response body for requests
 */
@UtilityClass
public class BodyGenerator {

    private static final Faker faker = new Faker();

    public static Long generateRandomId() {
        return faker.number().numberBetween(1L, 2000000L);
    }

    public static PetModel petObject() {
        return PetModel.builder()
                .id(generateRandomId())
                .build();
    }
}
