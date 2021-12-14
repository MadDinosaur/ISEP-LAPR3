package lapr.project.data;

import lapr.project.mappers.dto.UserDTO;

public class UserSqlStore implements Persistable {

    /**
     * Save an objet to the database.
     *
     * @param databaseConnection the database's connection
     * @param object             the object to be added
     * @return Operation success.
     */
    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return true;
    }

    /**
     * Delete an object from the database.
     *
     * @param databaseConnection the database's connection
     * @param object             the object to be deleted
     * @return Operation success.
     */
    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return true;
    }

    public int registerNewUserToDB(UserDTO userDTO) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return 11111;
    }

    private int generateNewRegistrationCode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
