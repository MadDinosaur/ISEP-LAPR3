package lapr.project.controller;

import lapr.project.data.MainStorage;
import lapr.project.mappers.dto.UserDTO;

public class RegisterNewUserController {

    private MainStorage mainStorage;

    public RegisterNewUserController() {
        this(MainStorage.getInstance());
    }

    public RegisterNewUserController(MainStorage mainStorage) {
        this.mainStorage = mainStorage;
    }

    public int registerNewUser(UserDTO userDTO) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
