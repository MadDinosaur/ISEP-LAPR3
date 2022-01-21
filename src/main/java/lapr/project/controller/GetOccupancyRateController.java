package lapr.project.controller;

import lapr.project.data.CargoManifestSqlStore;
import lapr.project.data.MainStorage;

import java.sql.SQLException;

public class GetOccupancyRateController {
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
    public GetOccupancyRateController() {
        this.mainStorage = MainStorage.getInstance();
        this.cargoManifestStore = new CargoManifestSqlStore();
    }

    /**
     * Creates a instance of the controller with the current storage instance and sql store
     *
     * @param mainStorage the storage instance used to store all information
     * @param cargoManifestSqlStore the store instance used to store database requests
     */
    public GetOccupancyRateController(MainStorage mainStorage, CargoManifestSqlStore cargoManifestSqlStore) {
        this.mainStorage = mainStorage;
        this.cargoManifestStore = cargoManifestSqlStore;
    }

    /**
     * gets the occupancy rate of a given ship and returns it
     * @param shipMmsi captain's ID
     * @param manifestId Cargo Manifest ID
     * @return occupancy rate
     */
    public double getOccupancyRate(int shipMmsi, int manifestId){
        try{
            return cargoManifestStore.getOccupancyRate(mainStorage.getDatabaseConnection(), shipMmsi, manifestId);
        }catch (SQLException throwables){
            throwables.printStackTrace();
            return 0;
        }
    }
}
