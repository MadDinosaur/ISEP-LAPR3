package lapr.project.data;

import lapr.project.model.Coordinate;
import lapr.project.model.Country;
import lapr.project.model.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CountrySqlStore implements Persistable {

    /**
     * Saves the country to the database
     * @param databaseConnection the database's connection
     * @param object the object to be added
     * @return true if the country is added
     */
    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Country country = (Country) object;
        boolean returnValue = false;

        try {
            returnValue = saveCountryToDatabase(databaseConnection, country);;

        } catch (SQLException ex) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, ex.getMessage());
            databaseConnection.registerError(ex);
        }

        return returnValue;
    }

    /**
     * deletes a storage from database
     * @param databaseConnection the database's connection
     * @param object the object to be deleted
     * @return true if the country was deleted
     */
    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        boolean returnValue = false;
        Connection connection = databaseConnection.getConnection();
        Country country = (Country) object;

        try {
            String sqlCommand;

            sqlCommand = "delete from Country where country = ?";
            try (PreparedStatement deleteCountryPreparedStatement = connection.prepareStatement(sqlCommand)) {
                deleteCountryPreparedStatement.setString(1, country.getCountry());
                deleteCountryPreparedStatement.executeUpdate();
            }

            BorderSQLStore borderSQLStore = new BorderSQLStore();
            returnValue = borderSQLStore.delete(databaseConnection, country.getCountry());

            sqlCommand = "delete from Storage where country_name = ?";
            try (PreparedStatement deleteCountryPreparedStatement = connection.prepareStatement(sqlCommand)) {
                deleteCountryPreparedStatement.setString(1, country.getCountry());
                deleteCountryPreparedStatement.executeUpdate();
            }

        } catch (SQLException exception) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
        }

        return returnValue;
    }

    /**
     * Checks is a country is already registered on the database. If the country
     * is registered it doesn't add it.
     *
     * @param databaseConnection Connection to the Database
     * @param country            an object of type Storage
     * @throws SQLException in case something goes wrong during the Database connection
     */
    private boolean saveCountryToDatabase(DatabaseConnection databaseConnection, Country country) throws SQLException {
        boolean returnValue = false;

        if (!isCountryOnDataBase(databaseConnection, country)) {
            Connection connection = databaseConnection.getConnection();
            String sqlCommand = "INSERT INTO Country(continent,alpha2,alpha3,country,population,capital,latitude,longitude) VALUES(?,?,?,?,?,?,?,?)";
            try(PreparedStatement saveCountryPreparedStatement = connection.prepareStatement(sqlCommand)) {

                saveCountryPreparedStatement.setString(1, country.getContinent());
                saveCountryPreparedStatement.setString(2, country.getAlpha2());
                saveCountryPreparedStatement.setString(3, country.getAlpha3());
                saveCountryPreparedStatement.setString(4, country.getCountry());
                saveCountryPreparedStatement.setFloat(5, country.getPopulation());
                saveCountryPreparedStatement.setString(6, country.getCapital());
                saveCountryPreparedStatement.setFloat(7, country.getCoordinate().getLatitude());
                saveCountryPreparedStatement.setFloat(8, country.getCoordinate().getLongitude());
                saveCountryPreparedStatement.executeUpdate();
                returnValue = true;
            }
        }
        return returnValue;
    }

    /**
     * Checks if a given country is already registered in the database
     *
     * @param databaseConnection Connection to the Database
     * @param country            an object of type Country
     * @return true if the country already exists
     */
    private boolean isCountryOnDataBase(DatabaseConnection databaseConnection, Country country) {
        Connection connection = databaseConnection.getConnection();

        try {
            String sqlCommand = "select * from country where country = ?";

            PreparedStatement getCountryPreparedStatement = connection.prepareStatement(sqlCommand);
            getCountryPreparedStatement.setString(1, country.getCountry());
            try (ResultSet countryResultSet = getCountryPreparedStatement.executeQuery()) {
                return countryResultSet.next();
            }

        } catch (SQLException exception) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
            return false;
        }
    }
}