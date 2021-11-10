package lapr.project.data;

import lapr.project.model.PositioningData;
import lapr.project.model.Ship;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DynamicDataSqlStore implements Persistable{

    private Ship ship;

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Connection connection = databaseConnection.getConnection();
        PositioningData dynamicData = (PositioningData) object;

        String sqlCommand = "insert into dynamicdata(ship_mmsi, base_date_time, latitude, longitude, sog, cog, heading, position, transceiver_class) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        boolean returnValue = false;
        try (PreparedStatement saveAddressPreparedStatement = connection.prepareStatement(sqlCommand)) {
            saveAddressPreparedStatement.setInt(1, Integer.parseInt(ship.getMmsi()));
            saveAddressPreparedStatement.setDate(2, (Date) dynamicData.getBdt());
            saveAddressPreparedStatement.setFloat(3, dynamicData.getCoordinate().getLatitude());
            saveAddressPreparedStatement.setFloat(4, dynamicData.getCoordinate().getLongitude());
            saveAddressPreparedStatement.setFloat(5, dynamicData.getSog());
            saveAddressPreparedStatement.setFloat(6, dynamicData.getCog());
            saveAddressPreparedStatement.setFloat(7, dynamicData.getHeading());
            if (!dynamicData.getPosition().contains("NA"))
                saveAddressPreparedStatement.setInt(8, Integer.parseInt(dynamicData.getPosition()));
            else
                saveAddressPreparedStatement.setNull(8, 0);
            saveAddressPreparedStatement.setString(9, dynamicData.getTransceiverClass());
            saveAddressPreparedStatement.executeUpdate();
            returnValue = true;
        } catch (SQLException ex) {
            Logger.getLogger(DynamicDataSqlStore.class.getName()).log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
            returnValue = false;
        }
        return returnValue;
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        Connection connection = databaseConnection.getConnection();
        PositioningData dynamicData = (PositioningData) object;

        boolean returnValue = false;
        try {
            String sqlCommand;
            sqlCommand = "delete from dynamicdata where ship_mmsi = ? and base_date_time = ?";
            try (PreparedStatement shipDynamicDataDeletePreparedStatement = connection.prepareStatement(sqlCommand)) {
                shipDynamicDataDeletePreparedStatement.setInt(1, Integer.parseInt(ship.getMmsi()));
                shipDynamicDataDeletePreparedStatement.setDate(2, (Date) dynamicData.getBdt());
                shipDynamicDataDeletePreparedStatement.executeUpdate();
                returnValue = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DynamicDataSqlStore.class.getName()).log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
            returnValue = false;
        }
        return returnValue;
    }
}
