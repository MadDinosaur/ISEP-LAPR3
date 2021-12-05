package lapr.project.controller;

import lapr.project.data.CargoManifestSqlStore;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;

import java.sql.SQLException;
import java.sql.Timestamp;
import oracle.ucp.util.Pair;


public class GetOccupancyRateGivenMomentController {
    /**
     * The current storage
     */
    private final MainStorage mainStorage;

    /**
     * The current sql cargo manifest store
     */
    private final CargoManifestSqlStore cargoManifestStore;

    /**
     * Calls the creator with a the current storage instance
     */
    public GetOccupancyRateGivenMomentController() {
        this.mainStorage = MainStorage.getInstance();
        this.cargoManifestStore = new CargoManifestSqlStore();
    }

    /**
     * Creates a instance of the controller with the current storage instance and sql store
     *
     * @param mainStorage the storage instance used to store all information
     * @param cargoManifestSqlStore the store instance used to store database requests
     */
    public GetOccupancyRateGivenMomentController(MainStorage mainStorage, CargoManifestSqlStore cargoManifestSqlStore) {
        this.mainStorage = mainStorage;
        this.cargoManifestStore = cargoManifestSqlStore;
    }

    /**
     * Searches the database for a desired ship to know the occupancy rate at a given moment
     * @param mmsi the ship's mmsi
     * @param givenMoment the moment to search for
     * @return the occupancy rate of the desired ship and moment
     */
    public Pair<String,Double> getOccupancyRateGivenMoment(int mmsi, String givenMoment){
        try{
            return cargoManifestStore.getOccupancyRateGivenMoment(mainStorage.getDatabaseConnection(), mmsi, givenMoment);
        }catch (SQLException throwable){
            throwable.printStackTrace();
            return null;
        }
    }
}
