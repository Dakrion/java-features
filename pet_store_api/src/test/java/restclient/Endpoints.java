package restclient;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Endpoints {

    public static final String PET_PATH = "/pet";

    public static final String GET_PET_PATH = PET_PATH + "/{petId}";
}