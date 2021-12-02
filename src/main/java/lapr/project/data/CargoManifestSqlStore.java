package lapr.project.data;

import oracle.ucp.util.Pair;

import java.sql.*;

public class CargoManifestSqlStore {

    /**
     * Searches the database for a ship whose captain as the same id of a registered captain
     * @param databaseConnection the current database connection
     * @param captainId the user captain's id
     * @param year the year being searched
     * @return a result set with all of the captains carried cargo manifest in a given year
     * @throws SQLException throws an exception if any of the commands is invalid
     */
    public static Pair<Integer, Integer> getCargoManifestInYear(DatabaseConnection databaseConnection, int captainId, int year) throws SQLException {
        Connection connection = databaseConnection.getConnection();
        String sqlCommand;

        sqlCommand = "SELECT COUNT(c.id) AS \"Transported Cargo Manifest\", AVG(count(cc.container_num)) AS \"Average Number of containers\"\n" +
                "    FROM container_cargoManifest cc, cargomanifest c, ship s, captain cp\n" +
                "    WHERE s.captain_id = ?\n" +
                "    AND s.captain_id = cp.id\n" +
                "    AND c.ship_mmsi = s.mmsi\n" +
                "    AND EXTRACT(YEAR FROM c.finishing_date_time) = ?\n" +
                "    AND cc.cargo_manifest_id = c.id\n" +
                "    GROUP BY c.id";
        try (PreparedStatement getManifestData = connection.prepareStatement(sqlCommand)) {
            getManifestData.setInt(1, captainId);
            getManifestData.setInt(2, year);
            try(ResultSet shipAddressesResultSet = getManifestData.executeQuery()) {
                if (shipAddressesResultSet.next())
                    return new Pair<>(shipAddressesResultSet.getInt(1), shipAddressesResultSet.getInt(2));
                else
                    return null;
            }
        }
    }

    /**
     * Searches the database for a desired ship to know the occupancy rate
     * @param databaseConnection the current database connection
     * @param ship_mmsi the ship's mmsi
     * @param manifest_id the cargo manifest id
     * @return the occupancy rate of a desired ship
     * @throws SQLException throws an exception if any of the commands is invalid
     */
    public static double getOccupancyRate(DatabaseConnection databaseConnection, int ship_mmsi, int manifest_id) throws SQLException{
        Connection connection = databaseConnection.getConnection();
        String sqlCommand;

        sqlCommand = "SELECT func_occupancy_rate(100000001,1)\n" +
                "as Occupancy_Rate, s.mmsi, s.imo, s.callsign, s.capacity, c.id  FROM SHIP s, CARGOMANIFEST c\n" +
                "WHERE s.mmsi = 100000001\n" +
                "AND c.id=1";

        try (PreparedStatement getManifestData = connection.prepareStatement(sqlCommand)) {
            getManifestData.setInt(1, ship_mmsi);
            getManifestData.setInt(2,manifest_id);
            getManifestData.setInt(3, ship_mmsi);
            getManifestData.setInt(4,manifest_id);
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
    public static Pair<String,Double> getOccupancyRateGivenMoment(DatabaseConnection databaseConnection, int mmsi,Timestamp givenMoment) throws SQLException{
        Connection connection = databaseConnection.getConnection();
        String sqlCommand;
        StringBuilder string = new StringBuilder();

        sqlCommand = "SELECT func_occupancy_rate_given_moment(?,?) as Occupancy_Rate, s.mmsi, s.imo, s.callsign, s.capacity FROM SHIP s\n" +
                "WHERE s.mmsi = ?";

        try(PreparedStatement getData = connection.prepareStatement(sqlCommand)){
            getData.setInt(1,mmsi);
            getData.setTimestamp(2,givenMoment);
            getData.setInt(3,mmsi);
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



}
