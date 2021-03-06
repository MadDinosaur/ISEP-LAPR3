package lapr.project.data;


import lapr.project.model.Coordinate;
import lapr.project.model.Storage;
import oracle.ucp.util.Pair;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

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
     * @param storage            an object of type Storage
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
     * @param storage            an object of type Storage
     * @return True if the Storage is registered, False if otherwise.
     * @throws SQLException in case something goes wrong during the Database connection
     */
    public boolean isStorageOnDatabase(DatabaseConnection databaseConnection, Storage storage) throws SQLException {
        Connection connection = databaseConnection.getConnection();

        boolean isStorageOnDatabase;

        String sqlCommandSelect = "select * from storage where identification = ?";

        try (PreparedStatement getStoragesPreparedStatement = connection.prepareStatement(sqlCommandSelect)) {

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
     * @param storage            an object of type Storage
     * @throws SQLException in case something goes wrong during the Database connection
     */
    private void insertStorageOnDatabase(DatabaseConnection databaseConnection, Storage storage) throws SQLException {
        String sqlCommand = "insert into storage(storage_type_id, name, country_name, latitude, longitude, identification, max_volume, SYSTEM_USER_CODE_MANAGER) values (?, ?, ?, ?, ?, ?, 500, '12')";

        executeStorageStatementOnDatabase(databaseConnection, storage, sqlCommand);
    }

    /**
     * Checks if a given country is already registered in the database
     *
     * @param databaseConnection Connection to the Database
     * @param country            an object of type Storage
     * @return true if the country already exists
     */
    private boolean existsCountry(DatabaseConnection databaseConnection, String country) {
        Connection connection = databaseConnection.getConnection();

        try {
            String sqlCommand = "select * from country where country = ?";

            try (PreparedStatement getStoragesPreparedStatement = connection.prepareStatement(sqlCommand)) {
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
     * @param storage            an object of type Storage
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
     * @param storage            an object of type Storage
     * @throws SQLException in case something goes wrong during the Database connection
     */
    private void executeStorageStatementOnDatabase(DatabaseConnection databaseConnection, Storage storage, String sqlCommand) throws SQLException {
        Connection connection = databaseConnection.getConnection();

        try (PreparedStatement saveStoragePreparedStatement = connection.prepareStatement(sqlCommand)) {

            //TODO: Hard-coded para colocar o storage-type como 1 (Port) visto que
            // apenas estamos a usar Ports por enquanto, enventualmente pode vir a ser necess??rio mudar isto
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
     *
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

    /**
     * gets the occupancy rate
     *
     * @param databaseConnection the connection to the database
     * @param storageId          the storage id
     * @return the occupancy rate
     * @throws SQLException in case something goes wrong during the Database connection
     */
    public Pair<Integer, Double> getOccupancyRate(DatabaseConnection databaseConnection, int storageId) throws SQLException {
        Connection connection = databaseConnection.getConnection();
        String sqlCommand;

        sqlCommand = "select storage.identification,func_occupancy_rate_storage(storage.identification)\n" +
                "from storage, dual\n" +
                "where storage.identification = ?";
        try (PreparedStatement getManifestData = connection.prepareStatement(sqlCommand)) {
            getManifestData.setInt(1, storageId);
            try (ResultSet shipAddressesResultSet = getManifestData.executeQuery()) {
                if (shipAddressesResultSet.next())
                    return new Pair<>(shipAddressesResultSet.getInt(1), shipAddressesResultSet.getDouble(2));
                else
                    return null;
            }
        }
    }

    /**
     * Gets the occupancy rate of a given storage during a given month.
     * Returns only the first date of the month and dates when the occupancy rates suffered changes.
     *
     * @param databaseConnection the connection to the database
     * @param storageId          the storage identification
     * @param month              the month to lookup
     * @param year               the year to lookup
     * @return a list of String composed of a date and the respective occupancy rate percentage
     */
    public List<String> getOccupancyMap(DatabaseConnection databaseConnection, int storageId, int month, int year) {
        List<String> occupancyMap = new ArrayList<>();

        Connection connection;
        try {
            connection = databaseConnection.getConnection();
        } catch (NullPointerException e) {
            return occupancyMap;
        }

        try (Statement s = connection.createStatement()) {
            //Enable DBMS_OUTPUT
            s.executeUpdate("begin dbms_output.enable(); end;");
            //Call procedure
            String sqlCommand = "begin occupation_map_given_month(?,?,?); end;";
            try (PreparedStatement occupancyMapPreparedStatement = connection.prepareStatement(sqlCommand)) {
                occupancyMapPreparedStatement.setInt(1, storageId);
                occupancyMapPreparedStatement.setInt(2, month);
                occupancyMapPreparedStatement.setInt(3, year);

                occupancyMapPreparedStatement.executeUpdate();

                // Fetch the SERVEROUTPUT explicitly, using DBMS_OUTPUT.GET_LINES
                try (CallableStatement call = connection.prepareCall(
                        "declare "
                                + "  num integer := 1000;"
                                + "begin "
                                + "  dbms_output.get_lines(?, num);"
                                + "end;"
                )) {
                    call.registerOutParameter(1, Types.ARRAY,
                            "DBMSOUTPUT_LINESARRAY");
                    call.execute();


                    Array a = call.getArray(1);
                    for (String occupancy : (String[]) a.getArray())
                        if (occupancy != null)
                            occupancyMap.add(occupancy);
                }
            }
            s.executeUpdate("begin dbms_output.disable(); end;");
        } catch (SQLException exception) {
            Logger.getLogger(ContainerSqlStore.class.getName()).log(Level.SEVERE, exception.getMessage());
            databaseConnection.registerError(exception);
        } catch (NullPointerException exception) {
            Logger.getLogger(ContainerSqlStore.class.getName()).log(Level.SEVERE, exception.getMessage());
        }
        return occupancyMap;
    }

    /**
     * Gets the loading and unloading info of all manifests managed by the user during the week following a given date.
     *
     * @param databaseConnection the connection to the database
     * @param portManagerId      the system user registration code
     * @param date               the timestamp to lookup
     * @return a list of String composed of manifests date and respective vehicle and container details
     */
    public List<String> getLoadingUnloadingMap(DatabaseConnection databaseConnection, String portManagerId, String date) {
        List<String> loadingUnloadingMap = new ArrayList<>();

        Connection connection;
        try {
            connection = databaseConnection.getConnection();
        } catch (NullPointerException e) {
            return loadingUnloadingMap;
        }


        String sqlCommand = "SELECT funcLoadingUnloadingMap (?, TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS')) FROM DUAL";
        try (PreparedStatement loadingUnloadingMapPreparedStatement = connection.prepareStatement(sqlCommand)) {
            loadingUnloadingMapPreparedStatement.setString(1, portManagerId);
            loadingUnloadingMapPreparedStatement.setString(2, date);

            try (ResultSet loadingUnloadingMapResultSet = loadingUnloadingMapPreparedStatement.executeQuery()) {
                ResultSet cursorResult = null;
                if (loadingUnloadingMapResultSet.next()) cursorResult = (ResultSet) loadingUnloadingMapResultSet.getObject(1);

                StringBuilder sb;
                while (cursorResult != null && cursorResult.next()) {
                    sb = new StringBuilder();
                    for (int i = 1; i <= cursorResult.getMetaData().getColumnCount(); i++)
                        sb.append(cursorResult.getString(i)).append("\t");
                    loadingUnloadingMap.add(sb.toString());
                }
            }
        } catch (SQLException exception) {
            Logger.getLogger(ContainerSqlStore.class.getName()).log(Level.SEVERE, exception.getMessage());
            databaseConnection.registerError(exception);
        } catch (NullPointerException exception) {
            Logger.getLogger(ContainerSqlStore.class.getName()).log(Level.SEVERE, exception.getMessage());
        }
        return loadingUnloadingMap;
    }

    /**
     * gets the number of leaving containers in 30 days
     *
     * @param databaseConnection the connection to the database
     * @param storageId          the storage id
     * @return the number of leaving containers
     * @throws SQLException in case something goes wrong during the Database connection
     */
    public Pair<Integer, Integer> getEstimateLeavingContainers30Days(DatabaseConnection databaseConnection, int storageId) throws SQLException {
        Connection connection = databaseConnection.getConnection();
        String sqlCommand;

        sqlCommand = "select storage.identification, func_estimate_number_leaving_containers(storage.identification)\n" +
                "from storage, dual\n" +
                "where storage.identification = ?";

        try (PreparedStatement getEstimate = connection.prepareStatement(sqlCommand)) {
            getEstimate.setInt(1, storageId);
            try (ResultSet resultSet = getEstimate.executeQuery()) {
                if (resultSet.next()) {
                    return new Pair<>(resultSet.getInt(1), resultSet.getInt(2));
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * gets a list of all the leaving container id
     *
     * @param databaseConnection the connection to the database
     * @param storageId          the storage id
     * @return list of leaving containers
     * @throws SQLException in case something goes wrong during the Database connection
     */
    public List<Integer> getContainers30Days(DatabaseConnection databaseConnection, int storageId) throws SQLException {
        Connection connection = databaseConnection.getConnection();
        List<Integer> containerList = new ArrayList<>();

        String sqlCommand = "select con.num\n" +
                "    FROM STORAGE s, CARGOMANIFEST_PARTIAL cp, container_cargomanifest cc, container con\n" +
                "    where s.identification = ?\n" +
                "    and s.identification = cp.storage_identification\n" +
                "    and cp.id = cc.partial_cargo_manifest_id\n" +
                "    and status like 'pending' \n" +
                "    and cp.finishing_date_time between current_timestamp and current_timestamp + 30\n" +
                "    and con.num = cc.container_num";

        try (PreparedStatement getStoragesPreparedStatement = connection.prepareStatement(sqlCommand)) {
            getStoragesPreparedStatement.setInt(1, storageId);
            try (ResultSet storagesResultSet = getStoragesPreparedStatement.executeQuery()) {
                while (storagesResultSet.next()) {
                    int num = storagesResultSet.getInt(1);
                    containerList.add(num);
                }
                return containerList;
            }
        } catch (SQLException exception) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
            return null;
        }
    }
}
