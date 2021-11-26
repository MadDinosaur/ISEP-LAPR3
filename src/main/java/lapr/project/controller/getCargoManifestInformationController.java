package lapr.project.controller;

import lapr.project.data.CargoManifestSqlStore;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import lapr.project.utils.ResultSetSize;

import java.sql.ResultSet;
import java.sql.SQLException;

public class getCargoManifestInformationController {
    /**
     * the current ship store
     */
    private final DatabaseConnection databaseConnection;

    /**
     * The ship whose dates are gonna be searched
     */
    private ResultSet cargoManifests;

    /**
     * Calls the creator with a the current storage instance
     */
    public getCargoManifestInformationController() {
        this(MainStorage.getInstance());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     *
     * @param mainStorage the storage instance used to store all information
     */
    public getCargoManifestInformationController(MainStorage mainStorage) {
        this.databaseConnection = mainStorage.getDatabaseConnection();
    }

    /**
     * sets up the controller with the amount of cargo manifests in a given year
     * @param captain_id the chosen captain's id
     * @param year the chosen year
     * @return true if the result is not null
     * @throws SQLException throws this exception if some of the values are nt within expected values
     */
    public boolean findCargoManifests(int captain_id, int year) throws SQLException {
        cargoManifests = CargoManifestSqlStore.getCargoManifestInYear(databaseConnection, captain_id, year);
        return cargoManifests != null;
    }


    /**
     * returns the number of cargo manifests
     * @return returns the number of cargo manifests
     * @throws SQLException throws this exception if some of the values are nt within expected values
     */
    public int getNumberOfCargoManifests() throws SQLException {
        if (cargoManifests != null){
            return ResultSetSize.size(cargoManifests);
        } else {
            return 0;
        }
    }

    /**
     * return the average amount of containers transported
     * @return return the average amount of containers transported
     * @throws SQLException throws this exception if some of the values are nt within expected values
     */
    public double getAverageNumberOfContainers() throws SQLException {
        if (cargoManifests != null){
            return CargoManifestSqlStore.averageContainer(databaseConnection, cargoManifests);
        } else {
            return 0;
        }
    }
}
