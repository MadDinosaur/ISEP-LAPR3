package lapr.project.data;

import lapr.project.model.Coordinate;
import lapr.project.model.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StorageSqlStore implements Persistable {

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Storage storage = (Storage) object;
        boolean returnValue = false;

        try {
            saveStorageToDatabase(databaseConnection, storage);

            //Save changes.
            returnValue = true;

        } catch (SQLException ex) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, ex.getMessage());
            databaseConnection.registerError(ex);
        }

        return returnValue;
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        boolean returnValue = false;
        Connection connection = databaseConnection.getConnection();
        Storage storage = (Storage) object;

        try {
            String sqlCommand;

            sqlCommand = "delete from storage where identification = ?";
            try (PreparedStatement deleteStoragePreparedStatement = connection.prepareStatement(sqlCommand)) {
                deleteStoragePreparedStatement.setInt(1, storage.getIdentification());
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
     * Checks is a Storage is already registered on the datase. If the Storage
     * is registered, it updates it. If it is not, it inserts a new one.
     *
     * @param databaseConnection Connection to the Database
     * @param storage an object of type Storage
     * @throws SQLException in case something goes wrong during the Database connection
     */
    private void saveStorageToDatabase(DatabaseConnection databaseConnection, Storage storage) throws SQLException {

        if (existsCountry(databaseConnection, storage.getCountry())) {
            if (isStorageOnDatabase(databaseConnection, storage)) {
                updateStorageOnDatabase(databaseConnection, storage);
            } else {
                insertStorageOnDatabase(databaseConnection, storage);
            }
        }
    }

    /**
     * Checks if a Storage is registered on the Database by its ID.
     *
     * @param databaseConnection Connection to the Database
     * @param storage an object of type Storage
     * @return True if the Storage is registered, False if otherwise.
     * @throws SQLException in case something goes wrong during the Database connection
     */
    public boolean isStorageOnDatabase(DatabaseConnection databaseConnection, Storage storage) throws SQLException {
        Connection connection = databaseConnection.getConnection();

        boolean isStorageOnDatabase;

        String sqlCommandSelect = "select * from storage where identification = ?";

        try(PreparedStatement getStoragesPreparedStatement = connection.prepareStatement(sqlCommandSelect)) {

            getStoragesPreparedStatement.setInt(1, storage.getIdentification());

            try (ResultSet storagesResultSet = getStoragesPreparedStatement.executeQuery()) {
                isStorageOnDatabase = storagesResultSet.next();
            }
        }
        
        return isStorageOnDatabase;
    }

    /**
     * Adds a new Storage record to the database.
     *
     * @param databaseConnection Connection to the Database
     * @param storage an object of type Storage
     * @throws SQLException in case something goes wrong during the Database connection
     */
    private void insertStorageOnDatabase(DatabaseConnection databaseConnection, Storage storage) throws SQLException {
        String sqlCommand = "insert into storage(storage_type_id, name, country_name, latitude, longitude, identification) values (?, ?, ?, ?, ?, ?)";

        executeStorageStatementOnDatabase(databaseConnection, storage, sqlCommand);
    }

    /**
     * Checks if a given country is already registered in the database
     * @param databaseConnection Connection to the Database
     * @param country an object of type Storage
     * @return true if the country already exists
     */
    private boolean existsCountry(DatabaseConnection databaseConnection, String country){
        Connection connection = databaseConnection.getConnection();

        try {
            String sqlCommand = "select * from country where country = ?";

            try(PreparedStatement getStoragesPreparedStatement = connection.prepareStatement(sqlCommand)) {
                getStoragesPreparedStatement.setString(1, country);
                try (ResultSet countryResultSet = getStoragesPreparedStatement.executeQuery()) {
                    return countryResultSet.next();
                }
            }

        } catch (SQLException exception) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
            return false;
        }
    }

    /**
     * Updates an existing Storage record on the database.
     *
     * @param databaseConnection Connection to the Database
     * @param storage an object of type Storage
     * @throws SQLException in case something goes wrong during the Database connection
     */
    private void updateStorageOnDatabase(DatabaseConnection databaseConnection, Storage storage) throws SQLException {
        String sqlCommand = "update storage set storage_type_id = ?, name = ?, country_name = ?, latitude = ?, longitude = ? where identification = ?";

        executeStorageStatementOnDatabase(databaseConnection, storage, sqlCommand);
    }

    /**
     * Executes the save Storage Statement.
     *
     * @param databaseConnection Connection to the Database
     * @param storage an object of type Storage
     * @throws SQLException in case something goes wrong during the Database connection
     */
    private void executeStorageStatementOnDatabase(DatabaseConnection databaseConnection, Storage storage, String sqlCommand) throws SQLException {
        Connection connection = databaseConnection.getConnection();

        try(PreparedStatement saveStoragePreparedStatement = connection.prepareStatement(sqlCommand)) {

            //TODO: Hard-coded para colocar o storage-type como 1 (Port) visto que
            // apenas estamos a usar Ports por enquanto, enventualmente pode vir a ser necessário mudar isto
            saveStoragePreparedStatement.setInt(1, 1);
            saveStoragePreparedStatement.setString(2, storage.getName());
            saveStoragePreparedStatement.setString(3, storage.getCountry());
            saveStoragePreparedStatement.setFloat(4, storage.getCoordinate().getLatitude());
            saveStoragePreparedStatement.setFloat(5, storage.getCoordinate().getLongitude());
            saveStoragePreparedStatement.setInt(6, storage.getIdentification());
            saveStoragePreparedStatement.executeUpdate();
        }
    }

    /**
     * Gets all the storages in the database and returns a list
     * @param databaseConnection the connection to the database
     * @return a list of storages
     */
    public List<Storage> getStorageDataFromDataBase(DatabaseConnection databaseConnection) {
        Connection connection = databaseConnection.getConnection();
        List<Storage> storageList = new ArrayList<>();

        try {
            String sqlCommand = "select s.identification, s.name, ct.continent, s.country_name, s.latitude, s.longitude  from storage s, country ct where s.country_name = ct.country";

            PreparedStatement getStoragesPreparedStatement = connection.prepareStatement(sqlCommand);

            try (ResultSet storagesResultSet = getStoragesPreparedStatement.executeQuery()) {
                while (storagesResultSet.next()) {
                    int identification = storagesResultSet.getInt(1);
                    String name = storagesResultSet.getString(2);
                    String continent = storagesResultSet.getString(3);
                    String country = storagesResultSet.getString(4);
                    float latitude = storagesResultSet.getFloat(5);
                    float longitude = storagesResultSet.getFloat(6);

                    Storage storage = new Storage(identification, name, continent, country, new Coordinate(longitude, latitude));
                    storageList.add(storage);
                }
                return storageList;
            }

        } catch (SQLException exception) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
            return null;
        }
    }
}
