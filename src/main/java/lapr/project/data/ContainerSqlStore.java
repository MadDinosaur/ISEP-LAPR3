package lapr.project.data;

import lapr.project.model.Container;
import oracle.sql.TIMESTAMP;
import oracle.ucp.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContainerSqlStore implements Persistable {
    /**
     * Save an objet to the database.
     *
     * @param databaseConnection the database's conection
     * @param object             the object to be added
     * @return Operation success.
     */
    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Connection connection;
        try {
            connection = databaseConnection.getConnection();
        } catch (NullPointerException e) {
            return false;
        }

        Container container = (Container) object;

        boolean returnValue = false;
        try {
            String sqlCommand;

            sqlCommand = "insert into container values (?,?,?,?,?,?,?,?)";
            try (PreparedStatement insertContainerPreparedStatement = connection.prepareStatement(sqlCommand)) {
                insertContainerPreparedStatement.setInt(1, container.getContainerNum());
                insertContainerPreparedStatement.setInt(2, container.getCheckDigit());
                insertContainerPreparedStatement.setString(3, container.getIsoCode());
                insertContainerPreparedStatement.setDouble(4, container.getGrossWeight());
                insertContainerPreparedStatement.setDouble(5, container.getTareWeight());
                insertContainerPreparedStatement.setDouble(6, container.getPayload());
                insertContainerPreparedStatement.setDouble(7, container.getMaxVolume());
                insertContainerPreparedStatement.setInt(8, container.isRefrigerated() ? 1 : 0);
                insertContainerPreparedStatement.executeUpdate();
            }

            returnValue = true;
        } catch (SQLException ex) {
            Logger.getLogger(ContainerSqlStore.class.getName())
                    .log(Level.SEVERE, ex.getMessage());
            databaseConnection.registerError(ex);
        }
        return returnValue;
    }

    /**
     * Delete an object from the database.
     *
     * @param databaseConnection the database's conection
     * @param object             the object to be deleted
     * @return Operation success.
     */
    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        Connection connection;
        try {
            connection = databaseConnection.getConnection();
        } catch (NullPointerException e) {
            return false;
        }

        boolean returnValue = false;
        Container container = (Container) object;

        try {
            String sqlCommand;

            sqlCommand = "delete from container where num = ?";
            try (PreparedStatement deleteContainerPreparedStatement = connection.prepareStatement(sqlCommand)) {
                deleteContainerPreparedStatement.setInt(1, container.getContainerNum());
                deleteContainerPreparedStatement.executeUpdate();
            }

            returnValue = true;

        } catch (SQLException exception) {
            Logger.getLogger(ContainerSqlStore.class.getName()).log(Level.SEVERE, exception.getMessage());
            databaseConnection.registerError(exception);
        }

        return returnValue;
    }

    /**
     * Checks if a given container is associated with a shipment requested from a given client.
     *
     * @param connection   the database connection to perform the query on
     * @param clientId     the requesting client registration code
     * @param containerNum the given container number
     * @return
     */
    private boolean checkContainerLeased(Connection connection, String clientId, int containerNum) {
        if (clientId == null || clientId.isEmpty()) return false;

        try {
            String sqlCommand = "SELECT * FROM Shipment\n" +
                    "WHERE container_num = ? AND system_user_code_client = ?";
            try (PreparedStatement selectContainerPreparedStatement = connection.prepareStatement(sqlCommand)) {
                selectContainerPreparedStatement.setInt(1, containerNum);
                selectContainerPreparedStatement.setString(2, clientId);

                try(ResultSet result = selectContainerPreparedStatement.executeQuery()) {
                    return result.next();
                }
            }
        } catch (SQLException | NullPointerException exception) {
            Logger.getLogger(ContainerSqlStore.class.getName()).log(Level.SEVERE, exception.getMessage());
        }
        return false;
    }

    /**
     * Returns the type and location of a given container
     *
     * @param databaseConnection the database connection to perform the query on
     * @param containerNum       the given container number
     * @return a list of information about the given container (number, type and location)
     */
    public List<String> getContainerStatus(DatabaseConnection databaseConnection, int containerNum) {
        List<String> resultOutput = new ArrayList<>();

        Connection connection;
        try {
            connection = databaseConnection.getConnection();
        } catch (NullPointerException e) {
            return resultOutput;
        }

        try {
            String sqlCommand = "SELECT * FROM(\n" +
                    "       SELECT CONTAINER.NUM as CONTAINER_NUM,\n" +
                    "           (CASE WHEN CP.LOADING_FLAG = 1 THEN 'Ship' ELSE 'Port' END) as LOCATION_TYPE,\n" +
                    "           (CASE WHEN CP.LOADING_FLAG = 1 THEN (SELECT NAME FROM SHIP WHERE MMSI = CP.SHIP_MMSI) ELSE (SELECT NAME FROM STORAGE WHERE IDENTIFICATION = CP.STORAGE_IDENTIFICATION) END) as LOCATION_NAME\n" +
                    "        FROM CONTAINER\n" +
                    "        INNER JOIN CONTAINER_CARGOMANIFEST ON CONTAINER_CARGOMANIFEST.CONTAINER_NUM = CONTAINER.NUM\n" +
                    "        INNER JOIN CARGOMANIFEST_PARTIAL CP on CONTAINER_CARGOMANIFEST.PARTIAL_CARGO_MANIFEST_ID = CP.ID\n" +
                    "        WHERE CONTAINER.NUM = ? AND CP.STATUS LIKE 'finished'\n" +
                    "        ORDER BY CP.FINISHING_DATE_TIME DESC)\n" +
                    "    WHERE ROWNUM = 1";
            try (PreparedStatement selectContainerPreparedStatement = connection.prepareStatement(sqlCommand)) {
                selectContainerPreparedStatement.setInt(1, containerNum);

                ResultSet result = selectContainerPreparedStatement.executeQuery();
                if (result.next()) {
                    resultOutput.add(result.getString(1));
                    resultOutput.add(result.getString(2));
                    resultOutput.add(result.getString(3));
                }
            }

        } catch (SQLException exception) {
            Logger.getLogger(ContainerSqlStore.class.getName()).log(Level.SEVERE, exception.getMessage());
            databaseConnection.registerError(exception);
        } catch (NullPointerException exception) {
            Logger.getLogger(ContainerSqlStore.class.getName()).log(Level.SEVERE, exception.getMessage());
        }
        return resultOutput;
    }

    /**
     * Returns the containers to be loaded/offloaded in a given port by a given ship,
     * including container identifier, type, position, and load.
     *
     * @param databaseConnection the database connection to perform the query on
     * @param shipMmsi           the given ship identifier
     * @param portId             the given port identifier
     * @param loading            flag to indicate a loading (true) or offloading (false) operation
     * @return a String matrix, where first row are headers and next rows are the respective values
     */
    public List<List<String>> getContainerManifest(DatabaseConnection databaseConnection, String shipMmsi, int portId, boolean loading) {
        List<List<String>> containers = new ArrayList<>();

        Connection connection;
        try {
            connection = databaseConnection.getConnection();
        } catch (NullPointerException e) {
            return containers;
        }

        try {
            String sqlCommand = "SELECT NUM,\n" +
                    "        (CASE WHEN REFRIGERATED_FLAG = 1 THEN 'Refrigerated' ELSE 'Non-refrigerated' END) as \"TYPE\",\n" +
                    "        CONTAINER_POSITION_X, CONTAINER_POSITION_Y, CONTAINER_POSITION_Z,\n" +
                    "        PAYLOAD\n" +
                    "        FROM CONTAINER INNER JOIN CONTAINER_CARGOMANIFEST ON CONTAINER.NUM = CONTAINER_CARGOMANIFEST.CONTAINER_NUM\n" +
                    "        WHERE PARTIAL_CARGO_MANIFEST_ID IN\n" +
                    "          (SELECT ID\n" +
                    "          FROM CARGOMANIFEST_PARTIAL\n" +
                    "          WHERE SHIP_MMSI = ?\n" +
                    "            AND STORAGE_IDENTIFICATION = ?\n" +
                    "            AND LOADING_FLAG = ?\n" +
                    "            AND STATUS LIKE 'finished')";
            try (PreparedStatement selectContainerPreparedStatement = connection.prepareStatement(sqlCommand)) {
                selectContainerPreparedStatement.setString(1, shipMmsi);
                selectContainerPreparedStatement.setInt(2, portId);
                selectContainerPreparedStatement.setInt(3, loading ? 1 : 0);

                ResultSet result = selectContainerPreparedStatement.executeQuery();
                ResultSetMetaData headers = result.getMetaData();
                //Set column headers
                containers.add(new ArrayList<>());
                for (int i = 1; i <= headers.getColumnCount(); i++)
                    containers.get(0).add(headers.getColumnName(i));
                //Set row values
                int j = 1;
                while (result.next()) {
                    containers.add(new ArrayList<>());
                    for (int i = 1; i <= headers.getColumnCount(); i++)
                        containers.get(j).add(result.getString(i));
                    j++;
                }
            }
        } catch (SQLException exception) {
            Logger.getLogger(ContainerSqlStore.class.getName()).log(Level.SEVERE, exception.getMessage());
            databaseConnection.registerError(exception);
        } catch (NullPointerException exception) {
            Logger.getLogger(ContainerSqlStore.class.getName()).log(Level.SEVERE, exception.getMessage());
        }
        return containers;
    }

    /**
     * Returns the list of operations performed on a given container in a given cargo manifest
     *
     * @param databaseConnection the database connection to perform the query on
     * @param containerNum       the given container number
     * @param cargoManifestId    the given cargo manifest identifier
     * @return a String matrix, where first row are headers and next rows are the respective values
     */
    public List<List<String>> getContainerAudit(DatabaseConnection databaseConnection, int containerNum, int cargoManifestId) {
        List<List<String>> auditLog = new ArrayList<>();

        Connection connection;
        try {
            connection = databaseConnection.getConnection();
        } catch (NullPointerException e) {
            return auditLog;
        }

        try {
            String sqlCommand = "SELECT * FROM Container_AuditTrail\n" +
                    "WHERE container_num = ? AND cargo_manifest_id = ?";
            try (PreparedStatement selectContainerPreparedStatement = connection.prepareStatement(sqlCommand)) {
                selectContainerPreparedStatement.setInt(1, containerNum);
                selectContainerPreparedStatement.setInt(2, cargoManifestId);

                try(ResultSet result = selectContainerPreparedStatement.executeQuery()) {
                    ResultSetMetaData headers = result.getMetaData();
                    //Set column headers
                    auditLog.add(new ArrayList<>());
                    for (int i = 1; i <= headers.getColumnCount(); i++)
                        auditLog.get(0).add(headers.getColumnName(i));
                    //Set row values
                    int j = 1;
                    while (result.next()) {
                        auditLog.add(new ArrayList<>());
                        for (int i = 1; i <= headers.getColumnCount(); i++)
                            auditLog.get(j).add(result.getString(i));
                        j++;
                    }
                }
            }
        } catch (SQLException exception) {
            Logger.getLogger(ContainerSqlStore.class.getName()).log(Level.SEVERE, exception.getMessage());
            databaseConnection.registerError(exception);
        } catch (NullPointerException exception) {
            Logger.getLogger(ContainerSqlStore.class.getName()).log(Level.SEVERE, exception.getMessage());
        }
        return auditLog;
    }

    /**
     * Returns the shipment identifier associated to a given container and client.
     *
     * @param databaseConnection the database connection to perform the query on
     * @param clientId           the requesting client registration code
     * @param containerNum       the given container number
     * @return shipment identifier or -1 if not found
     */
    public int getContainerShipment(DatabaseConnection databaseConnection, String clientId, int containerNum) {
        Connection connection;
        try {
            connection = databaseConnection.getConnection();
        } catch (NullPointerException e) {
            return -1;
        }

        if (!checkContainerLeased(connection, clientId, containerNum)) return -1;

        try {
            String sqlCommand = "SELECT id FROM Shipment\n" +
                    "WHERE container_num = ? AND system_user_code_client = ?";
            try (PreparedStatement selectContainerPreparedStatement = connection.prepareStatement(sqlCommand)) {
                selectContainerPreparedStatement.setInt(1, containerNum);
                selectContainerPreparedStatement.setString(2, clientId);

                try(ResultSet result = selectContainerPreparedStatement.executeQuery()) {
                    if (result.next()) return result.getInt(1);
                }
            }
        } catch (SQLException | NullPointerException exception) {
            Logger.getLogger(ContainerSqlStore.class.getName()).log(Level.SEVERE, exception.getMessage());
        }
        return -1;
    }

    /**
     * Returns a given container departure date.
     * @param connection the database connection to perform the query on
     * @param shipmentId the shipment identification associated to the container
     * @return the departure date or null if not found
     */
    private Timestamp getContainerDeparture(Connection connection, int shipmentId) {
        try {
            String sqlCommand = "SELECT PARTING_DATE FROM SHIPMENT WHERE ID = ?";
            try (PreparedStatement selectContainerPreparedStatement = connection.prepareStatement(sqlCommand)) {
                selectContainerPreparedStatement.setInt(1, shipmentId);

                try(ResultSet result = selectContainerPreparedStatement.executeQuery()) {

                    if (result.next())
                        return result.getTimestamp(1);
                }

            }
        } catch (SQLException | NullPointerException exception) {
            Logger.getLogger(ContainerSqlStore.class.getName()).log(Level.SEVERE, exception.getMessage());
        }
        return null;
    }

    /**
     * Returns a given container arrival date.
     * @param connection the database connection to perform the query on
     * @param shipmentId the shipment identification associated to the container
     * @return the arrival date or null if not found
     */
    private Timestamp getContainerArrival (Connection connection, int shipmentId) {
        try {
            String sqlCommand = "SELECT ARRIVAL_DATE FROM SHIPMENT WHERE ID = ?";
            try (PreparedStatement selectContainerPreparedStatement = connection.prepareStatement(sqlCommand)) {
                selectContainerPreparedStatement.setInt(1, shipmentId);

                try(ResultSet result = selectContainerPreparedStatement.executeQuery()) {

                    if (result.next())
                        return result.getTimestamp(1);
                }

            }
        } catch (SQLException | NullPointerException exception) {
            Logger.getLogger(ContainerSqlStore.class.getName()).log(Level.SEVERE, exception.getMessage());
        }
        return null;
    }

    /**
     * Returns a given container's path from source to current location indicating time of arrival and
     * departure at each location and mean of transport (ship or truck) between
     * each pair of locations.
     * @param databaseConnection the database connection to perform the query on
     * @param shipmentId the shipment identification associated to the container
     * @param containerNum the given container number
     * @return a String matrix, where first row are headers and next rows are the respective values
     */
    public List<List<String>> getContainerRoute(DatabaseConnection databaseConnection, int shipmentId,
                                                int containerNum) {
        List<List<String>> routeLog = new ArrayList<>();

        Connection connection;
        try { connection = databaseConnection.getConnection();
        } catch (NullPointerException e) { return routeLog; }

        Timestamp departureDate, arrivalDate;
        if ((departureDate = getContainerDeparture(connection, shipmentId)) == null) return null;
        if ((arrivalDate = getContainerArrival(connection, shipmentId)) == null) arrivalDate = new Timestamp(System.currentTimeMillis());

        try {
            String sqlCommand = "SELECT " +
                    "                (SELECT NAME FROM STORAGE WHERE IDENTIFICATION = STORAGE_IDENTIFICATION) as Location,\n" +
                    "                (CASE WHEN LOADING_FLAG = 1 THEN 'Loaded' ELSE 'Offloaded' END) as Operation,\n" +
                    "                (CASE WHEN SHIP_MMSI IS NOT NULL THEN 'Ship' ELSE 'Truck' END) as \"Mean of Transport\",\n" +
                    "                FINISHING_DATE_TIME as Timestamp\n" +
                    "        FROM CONTAINER_CARGOMANIFEST cc INNER JOIN CARGOMANIFEST_PARTIAL cp on cc.PARTIAL_CARGO_MANIFEST_ID = cp.ID\n" +
                    "        WHERE CONTAINER_NUM = ?\n" +
                    "        AND FINISHING_DATE_TIME BETWEEN ? AND ?\n" +
                    "        ORDER BY FINISHING_DATE_TIME";
            try (PreparedStatement selectContainerPreparedStatement = connection.prepareStatement(sqlCommand)) {
                selectContainerPreparedStatement.setInt(1, containerNum);
                selectContainerPreparedStatement.setTimestamp(2, departureDate);
                selectContainerPreparedStatement.setTimestamp(3, arrivalDate);

                try(ResultSet result = selectContainerPreparedStatement.executeQuery()) {
                    ResultSetMetaData headers = result.getMetaData();
                    //Set column headers
                    routeLog.add(new ArrayList<>());
                    for (int i = 1; i <= headers.getColumnCount(); i++)
                        routeLog.get(0).add(headers.getColumnName(i));
                    //Set row values
                    int j = 1;
                    while (result.next()) {
                        routeLog.add(new ArrayList<>());
                        for (int i = 1; i <= headers.getColumnCount(); i++)
                            routeLog.get(j).add(result.getString(i));
                        j++;
                    }
                }
            }
        } catch (SQLException exception) {
            Logger.getLogger(ContainerSqlStore.class.getName()).log(Level.SEVERE, exception.getMessage());
            databaseConnection.registerError(exception);
        } catch (NullPointerException exception) {
            Logger.getLogger(ContainerSqlStore.class.getName()).log(Level.SEVERE, exception.getMessage());
        }
        return routeLog;
    }
}
