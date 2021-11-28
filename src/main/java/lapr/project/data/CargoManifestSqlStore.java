package lapr.project.data;

import oracle.ucp.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public static int getOccupancyRate(DatabaseConnection databaseConnection, int captain_id, int manifest_id) throws SQLException{
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
