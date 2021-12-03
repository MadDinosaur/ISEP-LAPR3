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
     * @return a list of Pair<String, String>, where column headers are paired with their respective value
     */
    public List<Pair<String, String>> getContainerStatus(DatabaseConnection databaseConnection, int containerNum) {
        List<Pair<String, String>> rows = new ArrayList<>();

        Connection connection;
        try{ connection = databaseConnection.getConnection();
        } catch (NullPointerException e) { return rows; }

        try {
            String sqlCommand = "select * from (" +
                    "    select container.num as container_num," +
                    "           (case when cargomanifest.loading_flag = 1 then 'ship' else 'port' end) as location_type," +
                    "           (case when cargomanifest.loading_flag = 1 then (select name from ship where mmsi = cargomanifest.ship_mmsi) else (select name from storage where identification = cargomanifest.storage_identification) end) as location_name," +
                    "    from container" +
                    "    inner join container_cargomanifest on container_cargomanifest.container_num = container.num" +
                    "    inner join cargomanifest on container_cargomanifest.cargo_manifest_id = cargomanifest.id" +
                    "    where container.num = ? and cargomanifest.finishing_date_time is not null" +
                    "    order by cargomanifest.finishing_date_time desc)" +
                    "where rownum = 1";
            try (PreparedStatement selectContainerPreparedStatement = connection.prepareStatement(sqlCommand)) {
                selectContainerPreparedStatement.setInt(1, containerNum);

                ResultSet result = selectContainerPreparedStatement.executeQuery();
                ResultSetMetaData headers = result.getMetaData();
                for (int i = 1; i <= headers.getColumnCount(); i++)
                    rows.add(new Pair<>(headers.getColumnName(i), result.getString(i)));
            }

        } catch (SQLException exception) {
            Logger.getLogger(ContainerSqlStore.class.getName()).log(Level.SEVERE, exception.getMessage());
            databaseConnection.registerError(exception);
        } catch (NullPointerException exception) {
            Logger.getLogger(ContainerSqlStore.class.getName()).log(Level.SEVERE, exception.getMessage());
        }
        return rows;
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
            String sqlCommand = "select num as container_num," +
                    "       (case when refrigerated_flag = 1 then 'refrigerated' else 'non-refrigerated' end) as type," +
                    "       container_position_x, container_position_y, container_position_z," +
                    "       payload" +
                    "from container inner join container_cargomanifest on container.num = container_cargomanifest.container_num" +
                    "where cargo_manifest_id in" +
                    "      (select cargo_manifest_id" +
                    "      from cargomanifest" +
                    "      where ship_mmsi = ?" +
                    "        and cargomanifest.storage_identification = ?" +
                    "        and loading_flag = ?" +
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
