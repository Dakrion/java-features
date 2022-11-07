package tests;

import config.Initialization;
import dto.PetModel;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import tests.dataproviders.JsonToList;
import utils.ResponseWrapper;

import java.util.stream.Collectors;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("PetStore")
@Story("Pet")
public class GetPetByIdTests extends Initialization {

    private Long petId;

    private PetModel createPetBody;

//    @BeforeEach
//    void create_pet() {
//        createPetBody = petObject();
//
//        PetModel response = petApi.createPet(createPetBody)
//                .printResponseToConsole()
//                .assertStatusCode(HttpStatus.SC_OK)
//                .as(PetModel.class);
//
//        petId = response.getId();
//    }

    @Test
    @DisplayName("Успешное получение животного из магазина")
    public void success_get_pet_from_store() {
        PetModel response = petApi.getPet(petId)
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

        petApi.deletePet(petId)
                .assertStatusCode(HttpStatus.SC_OK);
    }

    @ParameterizedTest(name = "{displayName} - {index}")
    @DisplayName("Получение животного из json-файла")
    @MethodSource("tests.dataproviders.DataProviders#get_pet_from_json")
    void get_pet_from_json_use_provider(PetModel model) {
        PetModel response = petApi.getPet(model.getId())
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
    }

    @ParameterizedTest(name = "{displayName} - {index}")
    @DisplayName("Получение животного из json-файла")
    @CsvSource({"src/test/resources/data.json, dto.PetModel"})
    void get_pet_from_json_use_aggregator(@AggregateWith(JsonToList.class) PetModel[] models) {

        for (PetModel model : models) {
            PetModel response = petApi.getPet(model.getId())
                    .printResponseToConsole()
                    .assertStatusCode(HttpStatus.SC_OK)
                    .as(PetModel.class);

            step("Проверка ответа у питомца с id = " + model.getId(), () -> ResponseWrapper
                    .assertSoftly(softly -> {
                        softly.assertThat(response.getId()).withFailMessage("petId is null!")
                                .isNotNull();
                        softly.assertThat(response.getName()).withFailMessage("Pet name <%s> is not equal to <%s>", response.getName(), model.getName())
                                .isEqualTo(model.getName());
                        softly.assertThat(response.getStatus()).withFailMessage("Status <%s> is not equal to <%s>", response.getStatus(), model.getStatus())
                                .isEqualTo(model.getStatus());
                        softly.assertThat(response.getCategory().getName())
                                .withFailMessage("Category name <%s> is not equal to <%s>", response.getCategory().getName(), model.getCategory().getName())
                                .isEqualTo(model.getCategory().getName());
                        softly.assertThat(response.getCategory().getId())
                                .withFailMessage("Category id <%s> is not equal to <%s>", response.getCategory().getId(), model.getCategory().getId())
                                .isEqualTo(model.getCategory().getId());
                        softly.assertThat(response.getTags()).withFailMessage("Not find expected tags!").extracting("name", "id").asList().filteredOnAssertions(x -> {
                            assertThat(x).as("name").hasFieldOrPropertyWithValue("name", model.getTags().stream().map(PetModel.TagsItem::getName).collect(Collectors.toList()));
                            assertThat(x).as("id").hasFieldOrPropertyWithValue("id", model.getTags().stream().map(PetModel.TagsItem::getId).collect(Collectors.toList()));
                        });
                    }));
        }
    }
}
