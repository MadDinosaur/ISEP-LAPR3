package lapr.project.data;

import oracle.ucp.proxy.annotation.Pre;
import oracle.ucp.util.Pair;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CargoManifestSqlStore {

    /**
     * Searches the database for a ship whose captain as the same id of a registered captain
     * @param databaseConnection the current database connection
     * @param captainId the user captain's id
     * @param year the year being searched
     * @return a result set with all of the captains carried cargo manifest in a given year
     * @throws SQLException throws an exception if any of the commands is invalid
     */
    public Pair<Integer, Double> getCargoManifestInYear(DatabaseConnection databaseConnection, int captainId, int year) throws SQLException {
        Connection connection = databaseConnection.getConnection();
        String sqlCommand;

        sqlCommand = "SELECT COUNT(c.id), AVG(count(cc.container_num))\n" +
                "    INTO number_manifest, avg_manifest\n" +
                "    FROM container_cargoManifest cc, cargomanifest_partial c, ship s\n" +
                "         WHERE s.captain_id = ?\n" +
                "         AND c.ship_mmsi = s.mmsi\n" +
                "         AND EXTRACT(YEAR FROM c.finishing_date_time) = ?\n" +
                "         AND cc.partial_cargo_manifest_id = c.id\n" +
                "         GROUP BY c.id";
        try (PreparedStatement getManifestData = connection.prepareStatement(sqlCommand)) {
            getManifestData.setInt(1, captainId);
            getManifestData.setInt(2, year);
            try(ResultSet shipAddressesResultSet = getManifestData.executeQuery()) {
                if (shipAddressesResultSet.next())
                    return new Pair<>(shipAddressesResultSet.getInt(1), shipAddressesResultSet.getDouble(2));
                else
                    return null;
            }
        }
    }

    /**
     * Searches the database for a desired ship to know the occupancy rate
     * @param databaseConnection the current database connection
     * @param shipMmsi the ship's mmsi
     * @param manifestId the cargo manifest id
     * @return the occupancy rate of a desired ship
     * @throws SQLException throws an exception if any of the commands is invalid
     */
    public double getOccupancyRate(DatabaseConnection databaseConnection, int shipMmsi, int manifestId) throws SQLException{
        Connection connection = databaseConnection.getConnection();
        String sqlCommand;

        sqlCommand = "SELECT func_occupancy_rate(?,?) FROM DUAL";

        try (PreparedStatement getManifestData = connection.prepareStatement(sqlCommand)) {
            getManifestData.setInt(1, shipMmsi);
            getManifestData.setInt(2,manifestId);
            try(ResultSet occupancy_rate = getManifestData.executeQuery()) {
                if (occupancy_rate.next())
                    return occupancy_rate.getDouble(1);
                else
                    return 0;
            }
        }
    }

    /**
     * Searches the database for a desired ship to know the occupancy rate at a given moment and its data
     * @param databaseConnection the current database connection
     * @param mmsi the ship's mmsi
     * @param givenMoment the moment to search for
     * @return the occupancy rate of the desired ship and moment
     * @throws SQLException throws an exception if any of the commands is invalid
     */
    public Pair<String,Double> getOccupancyRateGivenMoment(DatabaseConnection databaseConnection, int mmsi,String givenMoment) throws SQLException{
        Connection connection = databaseConnection.getConnection();
        String sqlCommand;
        StringBuilder string = new StringBuilder();

        sqlCommand = "select func_occupancy_rate_given_moment(s.mmsi, TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS')) as Occupancy_Rate, s.mmsi, s.imo, s.callsign, s.capacity FROM SHIP s WHERE s.mmsi = ?";

        try(PreparedStatement getData = connection.prepareStatement(sqlCommand)){
            getData.setString(1,givenMoment);
            getData.setInt(2,mmsi);
            try(ResultSet occupancyRateSet = getData.executeQuery()){
                if (occupancyRateSet.next()) {
                    string.append("Ship's data - MMSI: ");
                    string.append(occupancyRateSet.getInt(2));
                    string.append(" IMO: ");
                    string.append(occupancyRateSet.getInt(3));
                    string.append(" Callsign: ");
                    string.append(occupancyRateSet.getString(4));
                    string.append(" Capacity: ");
                    string.append(occupancyRateSet.getDouble(5));
                    return new Pair<>(string.toString(), occupancyRateSet.getDouble(1));
                } else
                    return null;
            }
        }
    }

    /**
     * Gets a list of containers that belong to a given manifest
     * @param databaseConnection a connection to the database
     * @param manifestID the manifest's id
     * @return a list of containers from a given manifest
     */
    public List<String> getContainersGivenManifest(DatabaseConnection databaseConnection,int manifestID){
        Connection connection = databaseConnection.getConnection();
        List<String> containers = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(getShipMeasures(databaseConnection,manifestID));
        sb.append("\n");

        try{
            String sqlCommand = "SELECT c.container_num,c.container_position_x,c.container_position_y,c.container_position_z FROM container_cargoManifest c WHERE c.full_cargo_manifest_id =?";

            try (PreparedStatement getContainersStatement = connection.prepareStatement(sqlCommand)){
                getContainersStatement.setInt(1,manifestID);

                try (ResultSet containersSet = getContainersStatement.executeQuery()){
                    while(containersSet.next()){

                        sb.append(containersSet.getInt(1));
                        sb.append(",");
                        sb.append(containersSet.getInt(2));
                        sb.append(",");
                        sb.append(containersSet.getInt(3));
                        sb.append(",");
                        sb.append(containersSet.getInt(4));
                        containers.add(sb.toString());

                        sb = new StringBuilder();

                    }
                    return containers;
                }
            }

        }catch (SQLException exception){
            Logger.getLogger(CargoManifestSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
            return null;
        }
    }

    /**
     * Gets the width and length of tje ship for a given manifest
     * @param databaseConnection a connection to the database
     * @param manifestID The manifest's id
     * @return the string with the measures
     */
    private String getShipMeasures(DatabaseConnection databaseConnection, int manifestID){
        Connection connection = databaseConnection.getConnection();
        StringBuilder measures = new StringBuilder();

        try{
            String sqlCommand = "SELECT s.ship_width,s.ship_length \n" +
                    "FROM ship s \n" +
                    "WHERE s.mmsi = (SELECT c.ship_mmsi FROM cargomanifest_full c WHERE c.id = ?)";
            try (PreparedStatement getMeasuresStatement = connection.prepareStatement(sqlCommand)){
                getMeasuresStatement.setInt(1,manifestID);

                try(ResultSet measuresSet = getMeasuresStatement.executeQuery()) {
                    if (measuresSet.next()) {
                        measures.append(measuresSet.getDouble(1));
                        measures.append(",");
                        measures.append(measuresSet.getDouble(2));
                        return measures.toString();
                    }

                }
            }
        }catch (SQLException exception){
            Logger.getLogger(CargoManifestSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
            return null;
        }
        return null;
    }
}
