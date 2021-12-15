package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import lapr.project.data.UserSqlStore;
import lapr.project.mappers.dto.UserDTO;

public class RegisterNewUserController {

    private MainStorage mainStorage;

    public RegisterNewUserController() {
        this(MainStorage.getInstance());
    }

    public RegisterNewUserController(MainStorage mainStorage) {
        this.mainStorage = mainStorage;
    }

    public String registerNewUser(UserDTO userDTO) {
        DatabaseConnection dbconnection = mainStorage.getDatabaseConnection();
        UserSqlStore userSqlStore = new UserSqlStore();

        return userSqlStore.registerNewUserToDB(dbconnection, userDTO);
    }
}
