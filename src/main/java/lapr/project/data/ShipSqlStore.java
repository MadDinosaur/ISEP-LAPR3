package lapr.project.data;

import lapr.project.model.Ship;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShipSqlStore implements Persistable {
    
    /**
     * Save an objet to the database.
     *
     * @param databaseConnection the database's conection
     * @param object the object to be added
     * @return Operation success.
     */
    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Ship ship = (Ship) object;
        boolean returnValue = false;
        try {
            saveShipToDatabase(databaseConnection, ship);

            //Delete addresses.
            deleteShipAddresses(databaseConnection, ship);

            //Post new addresses.
            addShipAddresses(databaseConnection, ship);

            //Save changes.
            returnValue = true;

        } catch (SQLException ex) {
            Logger.getLogger(ShipSqlStore.class.getName())
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
            returnValue = false;
        }
        return returnValue;
    }

    private void deleteShipAddresses(DatabaseConnection databaseConnection, Ship ship) {
    }

    private void addShipAddresses(DatabaseConnection databaseConnection, Ship ship) {
    }

    private void saveShipToDatabase(DatabaseConnection databaseConnection, Ship ship)throws SQLException {
        if (isShipOnDatabase(databaseConnection, ship)) {
            updateShipOnDatabase(databaseConnection, ship);
        } else {
            insertShipOnDatabase(databaseConnection, ship);
        }
    }

    private void insertShipOnDatabase(DatabaseConnection databaseConnection, Ship ship) throws SQLException {
        Connection connection = databaseConnection.getConnection();
        String sqlCommand =
                "insert into ship(name, vatnr, id) values (?, ?, ?)";

        executeShipStatementOnDatabase(databaseConnection, ship, sqlCommand);
    }

    private void executeShipStatementOnDatabase(DatabaseConnection databaseConnection, Ship ship, String sqlCommand) {
    }

    private void updateShipOnDatabase(DatabaseConnection databaseConnection, Ship ship) {
    }

    private boolean isShipOnDatabase(DatabaseConnection databaseConnection, Ship ship)throws SQLException {
        Connection connection = databaseConnection.getConnection();

        String sqlCommand = "select * from ship where mmsi = ?";
        PreparedStatement getShipsPreparedStatement = connection.prepareStatement(sqlCommand);
        getShipsPreparedStatement.setInt(1, Integer.parseInt(ship.getMmsi()));

        try (ResultSet ShipsResultSet = getShipsPreparedStatement.executeQuery()) {
            return ShipsResultSet.next();
        }
    }


    /**
     * Delete an object from the database.
     *
     * @param databaseConnection the database's conection
     * @param object the object to be deleted
     * @return Operation success.
     */
    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        return false;
    }
}
