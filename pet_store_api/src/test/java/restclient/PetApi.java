package restclient;

import utils.ResponseWrapper;
import utils.annotations.helper.Service;
import utils.annotations.restspec.GET;
import utils.annotations.restspec.POST;

/**
 * Interface for api. Contains all api methods of service
 */
@Service("PetStore Service")
public interface PetApi {

    @GET(endpoint = "/pet/{petId}")
    ResponseWrapper getPet(Integer petId);

    @POST(endpoint = "/pet")
    <T> ResponseWrapper createPet(T body);
}
