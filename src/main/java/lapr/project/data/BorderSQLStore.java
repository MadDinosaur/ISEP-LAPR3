package lapr.project.data;

import lapr.project.model.Country;
import oracle.ucp.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BorderSQLStore implements Persistable {

    /**
     * saves the border to the database
     * @param databaseConnection the database's connection
     * @param object the object to be added
     * @return true if the border is added
     */
    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Pair<String, String> border = (Pair<String, String>) object;
        boolean returnValue = false;

        try {
            if (!isBorderOnDataBase(databaseConnection, border) && isCountryOnDataBase(databaseConnection, border)) {
                Connection connection = databaseConnection.getConnection();
                String sqlCommand = "insert into Border(country_name, country_border) values(?,?)";
                try(PreparedStatement saveCountryPreparedStatement = connection.prepareStatement(sqlCommand)) {
                    saveCountryPreparedStatement.setString(1, border.get1st());
                    saveCountryPreparedStatement.setString(2, border.get2nd());
                    saveCountryPreparedStatement.executeUpdate();
                }
                returnValue = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, ex.getMessage());
            databaseConnection.registerError(ex);
        }

        return returnValue;
    }

    /**
     * deletes a border in the database
     * @param databaseConnection the database's connection
     * @param object the object to be deleted
     * @return true if the border is deleted
     */
    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        boolean returnValue = false;
        Connection connection = databaseConnection.getConnection();
        String country = (String) object;

        try {
            String sqlCommand;

            sqlCommand = "delete from Border where country_name = ? or country_border = ?";
            try (PreparedStatement deleteStoragePreparedStatement = connection.prepareStatement(sqlCommand)) {
                deleteStoragePreparedStatement.setString(1, country);
                deleteStoragePreparedStatement.setString(2, country);
                deleteStoragePreparedStatement.executeUpdate();
            }

            returnValue = true;

        } catch (SQLException exception) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
        }

        return returnValue;
    }

    /**
     * checks whether the border is already in the database
     * @param databaseConnection the database's connection
     * @param border an object with both the country that make uo the border
     * @return true if the border is already on the database
     */
    private boolean isBorderOnDataBase(DatabaseConnection databaseConnection, Pair<String, String> border) {
        Connection connection = databaseConnection.getConnection();

        try {
            String sqlCommand = "select * from border where (country_name = ? and country_border = ?) or (country_name = ? and country_border = ?)";

            PreparedStatement getStoragesPreparedStatement = connection.prepareStatement(sqlCommand);
            getStoragesPreparedStatement.setString(1, border.get1st());
            getStoragesPreparedStatement.setString(2, border.get2nd());
            getStoragesPreparedStatement.setString(3, border.get2nd());
            getStoragesPreparedStatement.setString(4, border.get1st());
            try (ResultSet countryResultSet = getStoragesPreparedStatement.executeQuery()) {
                return countryResultSet.next();
            }

        } catch (SQLException exception) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
            return false;
        }
    }

    /**
     * Checks if a given country is already registered in the database
     *
     * @param databaseConnection Connection to the Database
     * @param border an object with the countries compromising the border
     * @return true if the country already exists
     */
    private boolean isCountryOnDataBase(DatabaseConnection databaseConnection, Pair<String, String> border) {
        Connection connection = databaseConnection.getConnection();

        try {
            String sqlCommand = "select * from country where country = ? or country = ?";

            PreparedStatement getStoragesPreparedStatement = connection.prepareStatement(sqlCommand);
            getStoragesPreparedStatement.setString(1, border.get1st());
            getStoragesPreparedStatement.setString(2, border.get2nd());
            try (ResultSet countryResultSet = getStoragesPreparedStatement.executeQuery()) {
                countryResultSet.next();
                return countryResultSet.next();
            }

        } catch (SQLException exception) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
            return false;
        }
    }
}