package lapr.project.controller;

import lapr.project.data.CargoManifestSqlStore;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import oracle.ucp.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GetCargoManifestInformationController {
    /**
     * the current ship store
     */
    private final DatabaseConnection databaseConnection;

    /**
     * Calls the creator with a the current storage instance
     */
    public GetCargoManifestInformationController() {
        this(MainStorage.getInstance());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     *
     * @param mainStorage the storage instance used to store all information
     */
    public GetCargoManifestInformationController(MainStorage mainStorage) {
        this.databaseConnection = mainStorage.getDatabaseConnection();
    }

    /**
     * sets up the controller with the amount of cargo manifests in a given year
     * @param captain_id the chosen captain's id
     * @param year the chosen year
     * @return true if the result is not null
     * @throws SQLException throws this exception if some of the values are nt within expected values
     */
    public Pair<Integer, Integer> findCargoManifests(int captain_id, int year) {
        try {
            return CargoManifestSqlStore.getCargoManifestInYear(databaseConnection, captain_id, year);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
