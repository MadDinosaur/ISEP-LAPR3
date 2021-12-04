package lapr.project.data;

import lapr.project.model.PositioningData;
import lapr.project.model.Ship;
import lapr.project.store.ShipStore;
import oracle.ucp.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

        String sqlCommand = "Select id from vesselType where id = ?";
        try(PreparedStatement existsVesselType = connection.prepareStatement(sqlCommand)) {
            existsVesselType.setInt(1, ship.getVesselType());
            try (ResultSet vesselTypeResultSet = existsVesselType.executeQuery()) {
                if (!vesselTypeResultSet.next()){
                    sqlCommand = "insert into vesselType(id) values(?)";
                    try(PreparedStatement VesselType = connection.prepareStatement(sqlCommand)) {
                        VesselType.setInt(1, ship.getVesselType());
                        VesselType.execute();
                    }
                }
            }
        }
        sqlCommand = "insert into ship(mmsi, fleet_id, captain_id, name, imo, num_generator, gen_power, callsign, vessel_type_id, ship_length, ship_width, capacity, draft) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


        try(PreparedStatement saveShipPreparedStatement = connection.prepareStatement(sqlCommand)) {
            saveShipPreparedStatement.setInt(1, Integer.parseInt(ship.getMmsi()));
            saveShipPreparedStatement.setInt(2, 1);
            saveShipPreparedStatement.setInt(3, Integer.parseInt(ship.getMmsi()));
            saveShipPreparedStatement.setString(4, ship.getShipName());
            saveShipPreparedStatement.setInt(5, ship.getImo());
            if (ship.getGenerator() != null) {
                saveShipPreparedStatement.setInt(6, ship.getGenerator().getNumberOfGenerators());
                saveShipPreparedStatement.setFloat(7, ship.getGenerator().getGeneratorOutput());
            } else {
                saveShipPreparedStatement.setInt(6, 0);
                saveShipPreparedStatement.setFloat(7, 0);
            }
            saveShipPreparedStatement.setString(8, ship.getCallSign());
            saveShipPreparedStatement.setInt(9, ship.getVesselType());
            saveShipPreparedStatement.setFloat(10, ship.getLength());
            saveShipPreparedStatement.setFloat(11, ship.getWidth());
            saveShipPreparedStatement.setFloat(12, ship.getCapacity());
            saveShipPreparedStatement.setFloat(13, ship.getDraft());
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
        String sqlCommand = "update ship set name = ?, ship_length = ?, ship_width = ?, draft = ? where mmsi = ?";

        try(PreparedStatement updateShipPreparedStatement = connection.prepareStatement(sqlCommand)) {
            updateShipPreparedStatement.setString(1, ship.getShipName());
            updateShipPreparedStatement.setFloat(2, ship.getLength());
            updateShipPreparedStatement.setFloat(3, ship.getWidth());
            updateShipPreparedStatement.setFloat(4, ship.getDraft());
            updateShipPreparedStatement.setInt(5, Integer.parseInt(ship.getMmsi()));
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

    /**
     * Loads all ships from the database into the shipStore class.
     * @param databaseConnection the database's connection
     * @param shipStore the ship store to insert ships into
     */
    public void loadShips(DatabaseConnection databaseConnection, ShipStore shipStore){
        Connection connection = databaseConnection.getConnection();
        String sqlCommand = "Select * from ship";
        try (PreparedStatement getShipData = connection.prepareStatement(sqlCommand)) {
            try (ResultSet shipData = getShipData.executeQuery()) {
                DynamicDataSqlStore dynamicDataSqlStore = new DynamicDataSqlStore();
                while(shipData.next()){
                    Ship ship = new Ship(shipData.getString("mmsi"), shipData.getString("name"),shipData.getInt("imo") ,
                            shipData.getString("callsign") , shipData.getInt("vessel_type_id"), shipData.getFloat("ship_length"),
                            shipData.getFloat("ship_width"),  shipData.getFloat("draft"));
                    ship.setPositioningDataList(dynamicDataSqlStore.loadDynamicData(databaseConnection, shipData.getString("mmsi")));
                    shipStore.addShip(ship);
                }
            }
        } catch (SQLException exception) {
            Logger.getLogger(ShipStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
        }
    }

    /**
     * Returns a ship object given a determined ship captain id.
     * @param databaseConnection the database's connection
     * @param captainId the ship captain in charge of the wanted ship
     * @return the ship of the given captain, or null if an error occurs
     */
    public Ship getShipByCaptainId(DatabaseConnection databaseConnection, String captainId) {
        Connection connection = databaseConnection.getConnection();
        String sqlCommand = "select * from ship where captain_id = ?";
        try (PreparedStatement getShipByCaptainId = connection.prepareStatement(sqlCommand)) {
            getShipByCaptainId.setString(1, captainId);
            try (ResultSet shipData = getShipByCaptainId.executeQuery()) {
                if(shipData.next())
                    return new Ship(shipData.getString("mmsi"), shipData.getString("name"),shipData.getInt("imo") ,
                            shipData.getString("callsign") , shipData.getInt("vessel_type_id"), shipData.getFloat("ship_length"),
                            shipData.getFloat("ship_width"),  shipData.getFloat("draft"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(ShipStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
        }
        return null;
    }

    /**
     * Fetches a list of ships that are gonna be available on the next monday, this list contains a Pair
     * that has the ship's mmsi and the Storage identification number where the ship will be located on monday
     *
     * @param databaseConnection a connection to the database
     * @return a list of ships that are available and their respective location
     */
    public List<Pair<Integer, Integer>> getAvailableShipsOnMonday(DatabaseConnection databaseConnection) {
        Connection connection = databaseConnection.getConnection();
        List<Pair<Integer, Integer>> availableShipAndLocationList = new ArrayList<>();

        try {
            String sqlCommand = "SELECT st1.ship_mmsi, st1.storage_identification_destination\n" +
                    "FROM shiptrip st1\n" +
                    "LEFT JOIN shiptrip st2\n" +
                    "    ON (\n" +
                    "        st1.ship_mmsi = st2.ship_mmsi \n" +
                    "        AND st1.arrival_date < st2.arrival_date\n" +
                    "    ) \n" +
                    "WHERE st2.arrival_date IS NULL \n" +
                    "    AND NEXT_DAY(CURRENT_DATE, 'SEGUNDA') > st1.arrival_date;";

            PreparedStatement getAvailableShipsPreparedStatement = connection.prepareStatement(sqlCommand);

            try (ResultSet availableShipsResultSet = getAvailableShipsPreparedStatement.executeQuery()) {
                while (availableShipsResultSet.next()) {
                    Pair<Integer, Integer> availableShipAndLocation = new Pair<>(availableShipsResultSet.getInt(1), availableShipsResultSet.getInt(2));
                    availableShipAndLocationList.add(availableShipAndLocation);
                }
                return availableShipAndLocationList;
            }

        } catch (SQLException exception) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
            return null;
        }
    }
}
