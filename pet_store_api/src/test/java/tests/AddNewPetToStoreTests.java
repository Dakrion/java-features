package tests;

import config.Initialization;
import dto.request.PetModel;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.ResponseWrapper;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static utils.BodyGenerator.petObject;

@Epic("PetStore")
@Story("Pet")
public class AddNewPetToStoreTests extends Initialization {

    private Long petId;

    @AfterEach
    void delete_pet_after_test() {
        petApi.deletePet(petId)
                .assertStatusCode(HttpStatus.SC_OK);
    }

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
                    softly.assertThat(response.getId()).withFailMessage("petId is null!")
                            .isNotNull();
                    softly.assertThat(response.getName()).withFailMessage("Pet name <%s> is not equal to <%s>", response.getName(), createPetBody.getName())
                            .isEqualTo(createPetBody.getName());
                    softly.assertThat(response.getStatus()).withFailMessage("Status <%s> is not equal to <%s>", response.getStatus(), createPetBody.getStatus())
                            .isEqualTo(createPetBody.getStatus());
                    softly.assertThat(response.getCategory().getName())
                            .withFailMessage("Category name <%s> is not equal to <%s>", response.getCategory().getName(), createPetBody.getCategory().getName())
                            .isEqualTo(createPetBody.getCategory().getName());
                    softly.assertThat(response.getCategory().getId())
                            .withFailMessage("Category id <%s> is not equal to <%s>", response.getCategory().getId(), createPetBody.getCategory().getId())
                            .isEqualTo(createPetBody.getCategory().getId());
                    softly.assertThat(response.getTags()).withFailMessage("Not find expected tags!").extracting("name", "id").asList().filteredOnAssertions(x -> {
                        assertThat(x).as("name").hasFieldOrPropertyWithValue("name", createPetBody.getTags().stream().map(PetModel.TagsItem::getName).collect(Collectors.toList()));
                        assertThat(x).as("id").hasFieldOrPropertyWithValue("id", createPetBody.getTags().stream().map(PetModel.TagsItem::getId).collect(Collectors.toList()));
                    });
                });

        petId = response.getId();
    }
}
