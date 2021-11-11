package lapr.project.data;

import lapr.project.model.PositioningData;
import lapr.project.model.Ship;
import lapr.project.store.ShipStore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
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
            deleteShipDynamicData(databaseConnection, ship);

            //Post new addresses.
            addShipDynamicData(databaseConnection, ship);

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

    /**
     * When deleting a ship this method deleted every single Dynamic data that is tied to it
     * @param databaseConnection the app's connection to the database
     * @param ship the ship from which the data will be deleted
     * @throws SQLException throws an exception if the command cannot be completed
     */
    private void deleteShipDynamicData(DatabaseConnection databaseConnection, Ship ship)  throws SQLException {
        Connection connection = databaseConnection.getConnection();
        String sqlCommand;

        //Delete addresses.
        sqlCommand = "select * from dynamicdata where ship_mmsi = ?";
        try (PreparedStatement getShipAddressesPreparedStatement = connection.prepareStatement(sqlCommand)) {
            getShipAddressesPreparedStatement.setInt(1, Integer.parseInt(ship.getMmsi()));
            try (ResultSet shipAddressesResultSet = getShipAddressesPreparedStatement.executeQuery()) {
                while (shipAddressesResultSet.next()) {

                    boolean found = false;
                    List<PositioningData> positioningDataList = (List<PositioningData>) ship.getPositioningDataList().inOrder();
                    for (int i = 0; i < positioningDataList.size() && !found; i++) {
                        PositioningData positioningData = positioningDataList.get(i);

                        Date bdt = shipAddressesResultSet.getTimestamp("base_date_time");

                        if (bdt.equals(positioningData.getBdt())) {
                            found = true;
                        }
                    }
                    if (!found) {

                        sqlCommand = "delete from dynamicdata where ship_mmsi = ? and base_date_time = ?";

                        try (PreparedStatement shipDynamicDataDeletePreparedStatement = connection.prepareStatement(sqlCommand)) {
                            shipDynamicDataDeletePreparedStatement.setInt(1, Integer.parseInt(ship.getMmsi()));
                            shipDynamicDataDeletePreparedStatement.setTimestamp(2, shipAddressesResultSet.getTimestamp("base_date_time"));
                            shipDynamicDataDeletePreparedStatement.executeUpdate();
                        }
                    }
                }
            }
        }
    }

    /**
     * Adds the ships dynamic data directly to the database
     * @param databaseConnection the app's connection to the database
     * @param ship the ship from which the data will be added
     * @throws SQLException throws an exception if the command is invalid
     */
    private void addShipDynamicData(DatabaseConnection databaseConnection, Ship ship)  throws SQLException {

        DynamicDataSqlStore dynamicDataSqlStore = new DynamicDataSqlStore();

        for (PositioningData positioningData : ship.getPositioningDataList().inOrder()) {
            dynamicDataSqlStore.setShip(ship);
            if (!dynamicDataSqlStore.save(databaseConnection, positioningData)) {
                throw databaseConnection.getLastError();
            }
        }
    }

    /**
     * checks id the ship already on the database and if so updates it otherwise saves it
     * @param databaseConnection the app's connection to the database
     * @param ship the ship to be added to the database
     * @throws SQLException throws an exception if one of the provided commands is invalid
     */
    private void saveShipToDatabase(DatabaseConnection databaseConnection, Ship ship)throws SQLException {
        if (isShipOnDatabase(databaseConnection, ship)) {
            updateShipOnDatabase(databaseConnection, ship);
        } else {
            insertShipOnDatabase(databaseConnection, ship);
        }
    }

    /**
     * Inserts the desired ship into the database along with it's dynamic data
     * @param databaseConnection the app's connection to the database
     * @param ship teh ship to be inserted into the database
     * @throws SQLException throws this exception if the command is invalid
     */
    private void insertShipOnDatabase(DatabaseConnection databaseConnection, Ship ship) throws SQLException {
        Connection connection = databaseConnection.getConnection();

        String sqlCommand = "insert into ship(mmsi, fleet_id, system_user_id_captain, system_user_id_chief_electrical_engineer" +
                ", name, imo, num_generator, gen_power, callsign, vessel_type, ship_length, ship_width, capacity, draft) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


        try(PreparedStatement saveShipPreparedStatement = connection.prepareStatement(sqlCommand)) {
            saveShipPreparedStatement.setInt(1, Integer.parseInt(ship.getMmsi()));
            saveShipPreparedStatement.setInt(2, 1);
            saveShipPreparedStatement.setInt(3, 1);
            saveShipPreparedStatement.setInt(4, 1);
            saveShipPreparedStatement.setString(5, ship.getShipName());
            saveShipPreparedStatement.setInt(6, ship.getImo());
            if (ship.getGenerator() != null) {
                saveShipPreparedStatement.setInt(7, ship.getGenerator().getNumberOfGenerators());
                saveShipPreparedStatement.setFloat(8, ship.getGenerator().getGeneratorOutput());
            } else {
                saveShipPreparedStatement.setInt(7, 0);
                saveShipPreparedStatement.setFloat(8, 0);
            }
            saveShipPreparedStatement.setString(9, ship.getCallSign());
            saveShipPreparedStatement.setInt(10, ship.getVesselType());
            saveShipPreparedStatement.setFloat(11, ship.getLength());
            saveShipPreparedStatement.setFloat(12, ship.getWidth());
            saveShipPreparedStatement.setFloat(13, ship.getCapacity());
            saveShipPreparedStatement.setFloat(14, ship.getDraft());
            saveShipPreparedStatement.executeUpdate();
        }
    }

    /**
     * if the ship is on the database this method updates it
     * @param databaseConnection the app's connection to the database
     * @param ship the ship to be updated
     * @throws SQLException throws an exception if the command is not valid
     */
    private void updateShipOnDatabase(DatabaseConnection databaseConnection, Ship ship)  throws SQLException {
        Connection connection = databaseConnection.getConnection();
        String sqlCommand = "update ship set name = ?, vessel_type = ?, ship_length = ?, ship_width = ?, draft = ? where mmsi = ?";

        try(PreparedStatement updateShipPreparedStatement = connection.prepareStatement(sqlCommand)) {
            updateShipPreparedStatement.setString(1, ship.getShipName());
            updateShipPreparedStatement.setInt(2, ship.getVesselType());
            updateShipPreparedStatement.setFloat(3, ship.getLength());
            updateShipPreparedStatement.setFloat(4, ship.getWidth());
            updateShipPreparedStatement.setFloat(5, ship.getDraft());
            updateShipPreparedStatement.setInt(6, Integer.parseInt(ship.getMmsi()));
            updateShipPreparedStatement.executeUpdate();
        }
    }

    /**
     * checks if the ship is already on the database
     * @param databaseConnection the app's connection to the database
     * @param ship the ship to be added to the database
     * @return true if the ship is already on the database
     * @throws SQLException throws an exception if the commande is not valid
     */
    private boolean isShipOnDatabase(DatabaseConnection databaseConnection, Ship ship)throws SQLException {
        Connection connection = databaseConnection.getConnection();

        String sqlCommand = "select * from ship where mmsi = ?";
        try(PreparedStatement getShipsPreparedStatement = connection.prepareStatement(sqlCommand)) {
            getShipsPreparedStatement.setInt(1, Integer.parseInt(ship.getMmsi()));

            try (ResultSet ShipsResultSet = getShipsPreparedStatement.executeQuery()) {
                return ShipsResultSet.next();
            }
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
        boolean returnValue = false;

        Connection connection = databaseConnection.getConnection();

        Ship ship = (Ship) object;

        try {
            String sqlCommand;

            sqlCommand = "delete from dynamicdata where ship_mmsi = ?";
            try (PreparedStatement deleteShipDynamicDataPreparedStatement = connection.prepareStatement(sqlCommand)) {
                deleteShipDynamicDataPreparedStatement.setInt(1, Integer.parseInt(ship.getMmsi()));
                deleteShipDynamicDataPreparedStatement.executeUpdate();
            }

            sqlCommand = "delete from ship where mmsi = ?";
            try (PreparedStatement deleteShipPreparedStatement = connection.prepareStatement(sqlCommand)) {
                deleteShipPreparedStatement.setInt(1, Integer.parseInt(ship.getMmsi()));
                deleteShipPreparedStatement.executeUpdate();
            }

            returnValue = true;

        } catch (SQLException exception) {
            Logger.getLogger(ShipStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
        }

        return returnValue;
    }
}
