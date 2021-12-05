package lapr.project.data;

import lapr.project.model.Container;
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
        try { connection = databaseConnection.getConnection();
        } catch (NullPointerException e) { return false; }

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
        try{ connection = databaseConnection.getConnection();
        } catch (NullPointerException e) { return false; }

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
     * Returns the type and location of a given container
     * @param databaseConnection the database connection to perform the query on
     * @param containerNum the given container number
     * @return a list of information about the given container (number, type and location)
     */
    public List<String> getContainerStatus(DatabaseConnection databaseConnection, int containerNum) {
        List<String> resultOutput = new ArrayList<>();

        Connection connection;
        try{ connection = databaseConnection.getConnection();
        } catch (NullPointerException e) { return resultOutput; }

        try {
            String sqlCommand = "SELECT * FROM (\n" +
                    "    SELECT CONTAINER.NUM as CONTAINER_NUM,\n" +
                    "           (CASE WHEN CARGOMANIFEST.LOADING_FLAG = 1 THEN 'Ship' ELSE 'Port' END) as LOCATION_TYPE,\n" +
                    "           (CASE WHEN CARGOMANIFEST.LOADING_FLAG = 1 THEN (SELECT NAME FROM SHIP WHERE MMSI = CARGOMANIFEST.SHIP_MMSI) ELSE (SELECT NAME FROM STORAGE WHERE IDENTIFICATION = CARGOMANIFEST.STORAGE_IDENTIFICATION) END) as LOCATION_NAME\n" +
                    "    FROM CONTAINER\n" +
                    "    INNER JOIN CONTAINER_CARGOMANIFEST ON CONTAINER_CARGOMANIFEST.CONTAINER_NUM = CONTAINER.NUM\n" +
                    "    INNER JOIN CARGOMANIFEST ON CONTAINER_CARGOMANIFEST.CARGO_MANIFEST_ID = CARGOMANIFEST.ID\n" +
                    "    WHERE CONTAINER.NUM = ? AND CARGOMANIFEST.FINISHING_DATE_TIME IS NOT NULL AND CARGOMANIFEST.LOADING_FLAG IS NOT NULL\n" +
                    "    ORDER BY CARGOMANIFEST.FINISHING_DATE_TIME DESC)\n" +
                    "WHERE ROWNUM = 1";
            try (PreparedStatement selectContainerPreparedStatement = connection.prepareStatement(sqlCommand)) {
                selectContainerPreparedStatement.setInt(1, containerNum);

                ResultSet result = selectContainerPreparedStatement.executeQuery();
                int i = 1; while(result.next()) {
                    resultOutput.add(result.getString(i)); i++;}
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
     * @param databaseConnection the database connection to perform the query on
     * @param shipMmsi the given ship identifier
     * @param portId the given port identifier
     * @param loading flag to indicate a loading (true) or offloading (false) operation
     * @return a String matrix, where first row are headers and next rows are the respective values
     */
    public List<List<String>> getContainerManifest(DatabaseConnection databaseConnection, String shipMmsi, int portId, boolean loading) {
        List<List<String>> containers = new ArrayList<>();

        Connection connection;
        try{ connection = databaseConnection.getConnection();
        } catch (NullPointerException e) { return containers; }

        try {
            String sqlCommand = "select num,\n" +
                    "       (case when refrigerated_flag = 1 then 'refrigerated' else 'non-refrigerated' end) as type,\n" +
                    "       container_position_x, container_position_y, container_position_z,\n" +
                    "       payload\n" +
                    "from container inner join container_cargomanifest on container.num = container_cargomanifest.container_num\n" +
                    "where cargo_manifest_id in\n" +
                    "      (select id\n" +
                    "      from cargomanifest\n" +
                    "      where ship_mmsi = ?\n" +
                    "        and cargomanifest.storage_identification = ?\n" +
                    "        and loading_flag = ?\n" +
                    "        and finishing_date_time is null)";
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
}
