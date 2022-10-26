package tests;

import config.Initialization;
import dto.request.PetModel;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.ResponseWrapper;

import static utils.BodyGenerator.petObject;

@Epic("PetStore")
@Story("Pet")
public class AddNewPetToStoreTests extends Initialization {

    @Test
    @DisplayName("Успешное добавление животного в магазин")
    public void success_add_pet_to_store() {
        PetModel createPetBody = petObject();

        PetModel response = petApi.createPet(createPetBody)
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
}
