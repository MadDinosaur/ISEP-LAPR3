package lapr.project.data;

import lapr.project.model.Container;
import lapr.project.model.Ship;
import lapr.project.store.ShipStore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        Connection connection = databaseConnection.getConnection();

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
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
            returnValue = false;
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
        boolean returnValue = false;

        Connection connection = databaseConnection.getConnection();

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
            Logger.getLogger(ContainerSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
        }

        return returnValue;
    }

    /**
     * Checks if a given container belongs to any shipment from a given client
     * @param databaseConnection the database connection to perform the query on
     * @param clientId the given client identification
     * @param containerNum the given container number
     * @return whether the container exists in shipment
     */
    public boolean checkContainerInShipment(DatabaseConnection databaseConnection, String clientId, int containerNum) {
        Connection connection = databaseConnection.getConnection();

        try {
            String sqlCommand;

            sqlCommand = "select * from shipment where clientId = ? and container_num = ?";
            try (PreparedStatement selectContainerPreparedStatement = connection.prepareStatement(sqlCommand)) {
                selectContainerPreparedStatement.setString(1, clientId);
                selectContainerPreparedStatement.setInt(2, containerNum);

                //checks if it returns any rows
                return selectContainerPreparedStatement.executeQuery().next();
            }

        } catch (SQLException exception) {
            Logger.getLogger(ContainerSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
        }
        return false;
    }

    /**
     * Returns the type and location of a given container
     * @param databaseConnection the database connection to perform the query on
     * @param containerNum the given container number
     * @return the type and the concrete instance of its current location
     */
    public String getContainerStatus(DatabaseConnection databaseConnection, int containerNum) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
