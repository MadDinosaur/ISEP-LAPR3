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

        sqlCommand = "Select count(c.id) as \"Transported Cargo Manifest\", avg(count(cc.container_num)) as \"Average Number of containers\"\n" +
                "from container_cargoManifest cc, cargomanifest c, ship s, captain cp\n" +
                "where s.captain_id = ?\n" +
                "and s.captain_id = cp.id\n" +
                "and c.ship_mmsi = s.mmsi\n" +
                "and extract(year from c.finishing_date_time) = ?\n" +
                "and cc.cargo_manifest_id = c.id\n" +
                "group by c.id";
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

        sqlCommand = "select func.occupancy_rate(?,?) FROM DUAL";

        try (PreparedStatement getManifestData = connection.prepareStatement(sqlCommand)) {
            getManifestData.setInt(1, ship_mmsi);
            getManifestData.setInt(2,manifest_id);
            try(ResultSet occupancy_rate = getManifestData.executeQuery()) {
                if (occupancy_rate.next())
                    return occupancy_rate.getDouble(1);
                else
                    return 0;
            }
        }
    }

    /**
     * Searches the database for a desired ship to know the occupancy rate at a given moment
     * @param databaseConnection the current database connection
     * @param mmsi the ship's mmsi
     * @param givenMoment the moment to search for
     * @return the occupancy rate of the desired ship and moment
     * @throws SQLException throws an exception if any of the commands is invalid
     */
    public static double getOccupancyRateGivenMoment(DatabaseConnection databaseConnection, int mmsi,Timestamp givenMoment) throws SQLException{
        Connection connection = databaseConnection.getConnection();
        String sqlCommand;

        sqlCommand = "SELECT func_occupancy_rate_given_moment(?,?) as Occupancy_Rate, s.mmsi, s.imo, s.callsign, s.capacity FROM SHIP s\n" +
                "WHERE s.mmsi = ?";

        try(PreparedStatement getData = connection.prepareStatement(sqlCommand)){
            getData.setInt(1,mmsi);
            getData.setTimestamp(2,givenMoment);
            getData.setInt(3,mmsi);
            try(ResultSet occupancyRateSet = getData.executeQuery()){
                if (occupancyRateSet.next())
                    return occupancyRateSet.getDouble(1);
                else
                    return 0;
            }
        }
    }



}
