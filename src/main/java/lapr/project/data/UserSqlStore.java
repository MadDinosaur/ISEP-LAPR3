package lapr.project.data;

import lapr.project.mappers.dto.UserDTO;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserSqlStore implements Persistable {

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        UserDTO userDTO = (UserDTO) object;
        boolean returnValue = false;

        try {
            saveUserToDatabase(databaseConnection, userDTO);

            //Save changes.
            returnValue = true;

        } catch (SQLException ex) {
            Logger.getLogger(UserSqlStore.class.getName()).log(Level.SEVERE, ex.getMessage());
            databaseConnection.registerError(ex);
        }

        return returnValue;
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        boolean returnValue = false;
        Connection connection = databaseConnection.getConnection();
        UserDTO userDTO = (UserDTO) object;

        try {
            String sqlCommand;

            sqlCommand = "delete from systemuser where registration_code = ?";
            try (PreparedStatement deleteUserPreparedStatement = connection.prepareStatement(sqlCommand)) {
                deleteUserPreparedStatement.setString(1, userDTO.getRegistrationCode());
                deleteUserPreparedStatement.executeUpdate();
            }

            returnValue = true;

        } catch (SQLException exception) {
            Logger.getLogger(UserSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
        }

        return returnValue;
    }

    /**
     * Checks is a User is already registered on the database. If the User
     * is registered, it updates it. If it is not, it inserts a new one.
     *
     * @param databaseConnection Connection to the Database
     * @param userDTO an object of type UserDTO
     * @throws SQLException in case something goes wrong during the Database connection
     */
    private void saveUserToDatabase(DatabaseConnection databaseConnection, UserDTO userDTO) throws SQLException {

        if (existsRole(databaseConnection, userDTO.getRole())) {
            if (isUserOnDatabase(databaseConnection, userDTO.getEmail())) {
                updateUserOnDatabase(databaseConnection, userDTO);
            } else {
                insertUserOnDatabase(databaseConnection, userDTO);
            }
        }
    }

    /**
     * Checks if a User is registered on the Database by their Registration Code.
     *
     * @param databaseConnection Connection to the Database
     * @param email a String with the user's email
     * @return True if the User is registered, False if otherwise.
     * @throws SQLException in case something goes wrong during the Database connection
     */
    public boolean isUserOnDatabase(DatabaseConnection databaseConnection, String email) throws SQLException {
        Connection connection = databaseConnection.getConnection();

        boolean isUserOnDatabase;

        String sqlCommandSelect = "select * from systemuser where email = ?";

        try(PreparedStatement getUsersPreparedStatement = connection.prepareStatement(sqlCommandSelect)) {

            getUsersPreparedStatement.setString(1, email);

            try (ResultSet userResultSet = getUsersPreparedStatement.executeQuery()) {
                isUserOnDatabase = userResultSet.next();
            }
        }

        return isUserOnDatabase;
    }

    /**
     * Adds a new User record to the database.
     *
     * @param databaseConnection Connection to the Database
     * @param userDTO an object of type UserDTO
     * @throws SQLException in case something goes wrong during the Database connection
     */
    private void insertUserOnDatabase(DatabaseConnection databaseConnection, UserDTO userDTO) throws SQLException {
        String sqlCommand = "insert into systemuser(name, email, role_id, registration_code) values (?, ?, ?, ?)";

        executeUserStatementOnDatabase(databaseConnection, userDTO, sqlCommand);
    }

    /**
     * Checks if a given role is already registered in the database
     * @param databaseConnection Connection to the Database
     * @param role a String with the role id
     * @return true if the role already exists
     */
    private boolean existsRole(DatabaseConnection databaseConnection, String role){
        Connection connection = databaseConnection.getConnection();

        try {
            String sqlCommandSelectRoles = "select * from role where id = ?";

            try(PreparedStatement getRolesPreparedStatement = connection.prepareStatement(sqlCommandSelectRoles)) {
                getRolesPreparedStatement.setString(1, role);
                try (ResultSet roleResultSet = getRolesPreparedStatement.executeQuery()) {
                    return roleResultSet.next();
                }
            }

        } catch (SQLException exception) {
            Logger.getLogger(UserSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
            return false;
        }
    }

    /**
     * Updates an existing User record on the database.
     *
     * @param databaseConnection Connection to the Database
     * @param userDTO an object of type UserDTO
     * @throws SQLException in case something goes wrong during the Database connection
     */
    private void updateUserOnDatabase(DatabaseConnection databaseConnection, UserDTO userDTO) throws SQLException {
        userDTO.setRegistrationCode(getUserCode(databaseConnection, userDTO));

        String sqlCommand = "update systemuser set name = ?, email = ?, role_id = ? where registration_code = ?";

        executeUserStatementOnDatabase(databaseConnection, userDTO, sqlCommand);
    }

    /**
     * Executes the save User Statement.
     *
     * @param databaseConnection Connection to the Database
     * @param userDTO an object of type UserDTO
     * @throws SQLException in case something goes wrong during the Database connection
     */
    private void executeUserStatementOnDatabase(DatabaseConnection databaseConnection, UserDTO userDTO, String sqlCommand) throws SQLException {
        Connection connection = databaseConnection.getConnection();

        try(PreparedStatement saveUserPreparedStatement = connection.prepareStatement(sqlCommand)) {
            saveUserPreparedStatement.setString(1, userDTO.getUserName());
            saveUserPreparedStatement.setString(2, userDTO.getEmail());
            saveUserPreparedStatement.setString(3, userDTO.getRole());
            saveUserPreparedStatement.setString(4, userDTO.getRegistrationCode());
            saveUserPreparedStatement.executeUpdate();
        }
    }

    private String getUserCode(DatabaseConnection dbconnection, UserDTO userDTO) {
        Connection connection = dbconnection.getConnection();

        try {
            String sqlCommandSelectUserCode = "select registration_code from systemuser where email = ?";

            try(PreparedStatement getUserRole = connection.prepareStatement(sqlCommandSelectUserCode)) {
                getUserRole.setString(1, userDTO.getEmail());
                try (ResultSet userResultSet = getUserRole.executeQuery()) {
                    if (userResultSet.next())
                        return userResultSet.getString(1);
                }
            }

        } catch (SQLException exception) {
            Logger.getLogger(UserSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            dbconnection.registerError(exception);
            return null;
        }
        return null;
    }

    public String registerNewUserToDB(DatabaseConnection dbconnection, UserDTO userDTO) {
        userDTO.setRegistrationCode("null");
        save(dbconnection, userDTO);
        return getUserCode(dbconnection, userDTO);
    }
}
