package lapr.project.data;

import lapr.project.utils.ResultSetSize;

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
    public static ResultSet getCargosManifestInYear(DatabaseConnection databaseConnection, int captainId, int year) throws SQLException {
        Connection connection = databaseConnection.getConnection();
        String sqlCommand;

        sqlCommand = "select * from ship where captain_id = ?";
        try (PreparedStatement getShipByCaptain = connection.prepareStatement(sqlCommand)) {
            getShipByCaptain.setInt(1, captainId);
            try(ResultSet shipAddressesResultSet = getShipByCaptain.executeQuery()) {
                if (shipAddressesResultSet.next()) {
                    sqlCommand = "select * from CargoManifest where ship_mmsi = ? and Year(finishing_date_time) = ?";
                    try (PreparedStatement shipCargoManifest = connection.prepareStatement(sqlCommand)) {
                        shipCargoManifest.setInt(1, shipAddressesResultSet.getInt("mmsi"));
                        shipCargoManifest.setInt(2, year);
                        try (ResultSet shipCargoManifestResult = shipCargoManifest.executeQuery()) {
                            return shipCargoManifestResult;
                        }
                    }
                }
                return null;
            }
        }
    }

    /**
     * The method returns the amount of containers a set of cargo manifests has
     * @param databaseConnection the current database connection
     * @param cargoManifests a list of all cargo manifests
     * @return returns a number
     * @throws SQLException throws an exception if any of the commands is invalid
     */
    public static int numberOfContainer(DatabaseConnection databaseConnection, ResultSet cargoManifests) throws SQLException {
        Connection connection = databaseConnection.getConnection();
        String sqlCommand = "select count(*) from Container_CargoManifest where cargo_manifest_id = ?";
        int total = 0;

        while(cargoManifests.next()){
            try(PreparedStatement getContainerCargoManifest = connection.prepareStatement(sqlCommand)){
                getContainerCargoManifest.setInt(1, cargoManifests.getInt(1));
                try (ResultSet manifestContainers = getContainerCargoManifest.executeQuery()) {
                    total += manifestContainers.getInt(1);
                }
            }
        }
        return total;
    }

    public static double averageContainer(DatabaseConnection databaseConnection, ResultSet cargoManifests) throws SQLException {
        double total = numberOfContainer(databaseConnection, cargoManifests);
        return total / ResultSetSize.size(cargoManifests);
    }
}
