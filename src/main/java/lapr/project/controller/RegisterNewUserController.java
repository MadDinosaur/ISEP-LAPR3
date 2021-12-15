package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import lapr.project.data.UserSqlStore;
import lapr.project.mappers.dto.UserDTO;

public class RegisterNewUserController {

    /**
     * The current MainStorage
     */
    private MainStorage mainStorage;

    /**
     * Creates a instance of the controller with the current mainStorage instance
     */
    public RegisterNewUserController() {
        this(MainStorage.getInstance());
    }

    /**
     * Creates a instance of the controller with the given mainStorage instance
     * @param mainStorage the storage instance used to store all information
     */
    public RegisterNewUserController(MainStorage mainStorage) {
        this.mainStorage = mainStorage;
    }

    /**
     * Registers a new user to the database and returns a String containing the user's registration code
     * @param userDTO a UserDTO containing the information for the user that will be added
     * @return a String with the generated registration code
     */
    public String registerNewUser(UserDTO userDTO) {
        DatabaseConnection dbconnection = mainStorage.getDatabaseConnection();
        UserSqlStore userSqlStore = new UserSqlStore();

        return userSqlStore.registerNewUserToDB(dbconnection, userDTO);
    }
}
