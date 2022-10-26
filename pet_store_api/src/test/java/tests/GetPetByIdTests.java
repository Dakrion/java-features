package tests;

import com.fasterxml.jackson.core.type.TypeReference;
import config.Initialization;
import dto.request.PetModel;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utils.ResponseWrapper;
import utils.annotations.helper.Provider;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static utils.BodyGenerator.petObject;

@Epic("PetStore")
@Story("Pet")
public class GetPetByIdTests extends Initialization {

    private Integer petId;

    private PetModel createPetBody;

    @BeforeEach
    void createPet() {
        createPetBody = petObject();

        PetModel response = petApi.createPet(createPetBody)
                .printResponseToConsole()
                .assertStatusCode(HttpStatus.SC_OK)
                .as(PetModel.class);

        petId = response.getId().intValue();
    }

    @Test
    @DisplayName("Успешное получение животного из магазина")
    public void success_get_pet_from_store() {
        PetModel response = petApi.getPet(petId)
                .printResponseToConsole()
                .assertStatusCode(HttpStatus.SC_OK)
                .as(PetModel.class);

        ResponseWrapper
                .assertSoftly(softly -> {
                    softly.assertThat(response.getId()).withFailMessage("Response is null!")
                            .isNotNull();
                    softly.assertThat(response.getName()).withFailMessage("Pet name <%s> is not equal to <%s>", response.getName(), createPetBody.getName())
                            .isEqualTo(createPetBody.getName());
                    softly.assertThat(response.getStatus()).withFailMessage("Status <%s> is not equal to <%s>", response.getStatus(), createPetBody.getStatus())
                            .isEqualTo(createPetBody.getStatus());
                });
    }

    @ParameterizedTest(name = "{displayName} - {index}")
    @DisplayName("Получение животного из json-файла")
    @MethodSource("getDataFromJsonFile")
    void get_pet_from_json(PetModel model) {
        PetModel response = petApi.getPet(model.getId().intValue())
                .printResponseToConsole()
                .assertStatusCode(HttpStatus.SC_OK)
                .as(PetModel.class);

        ResponseWrapper
                .assertSoftly(softly -> {
                    softly.assertThat(response.getId()).withFailMessage("Response is null!")
                            .isNotNull();
                    softly.assertThat(response.getName()).withFailMessage("Pet name <%s> is not equal to <%s>", response.getName(), createPetBody.getName())
                            .isEqualTo(createPetBody.getName());
                    softly.assertThat(response.getStatus()).withFailMessage("Status <%s> is not equal to <%s>", response.getStatus(), createPetBody.getStatus())
                            .isEqualTo(createPetBody.getStatus());
                });
    }

    /**
     * provider for this test-class.
     * @return List
     */
    @Provider(testMode = Provider.TestMode.POSITIVE)
    static List<PetModel> getDataFromJsonFile() {
        try {
            return objectMapper.readValue(new File("src/test/resources/data.json"), new TypeReference<>() {});
        } catch (IOException ex) {
            System.out.println("File with this path or name not found!");
            ex.printStackTrace();
        }
        throw new NullPointerException("List is null!");
    }
}
