package tests.dataproviders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.request.PetModel;
import lombok.experimental.UtilityClass;
import utils.annotations.helper.Provider;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Class with all data providers for parameterized tests
 */
@UtilityClass
public class DataProviders {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Provider(testClass = "GetPetByIdTests", testMethod = "get_pet_from_json", testMode = Provider.TestMode.POSITIVE)
    static List<PetModel> get_pet_from_json() {
        try {
            return objectMapper.readValue(new File("src/test/resources/data.json"), new TypeReference<>() {});
        } catch (IOException ex) {
            System.out.println("File with this path or name not found!");
            ex.printStackTrace();
        }
        throw new NullPointerException("List is null!");
    }
}
