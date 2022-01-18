package lapr.project.controller;

import lapr.project.data.CargoManifestSqlStore;
import lapr.project.data.MainStorage;

import java.sql.SQLException;

public class AverageOccupancyRateController {
    /**
     *  The current main store
     */
    private final MainStorage mainStorage;

    /**
     * The current sql cargo manifest store
     */
    private final CargoManifestSqlStore cargoManifestStore;

    /**
     * Calls the creator with the current storage instance
     */
    public AverageOccupancyRateController(){
        this.mainStorage = MainStorage.getInstance();
        this.cargoManifestStore = new CargoManifestSqlStore();
    }

    /**
     * Creates a instance of the controller with the current storage instance
     * @param mainStorage the storage instance used to store all information
     */
    public AverageOccupancyRateController(MainStorage mainStorage, CargoManifestSqlStore cargoManifestSqlStore) {
        this.mainStorage = mainStorage;
        this.cargoManifestStore = cargoManifestSqlStore;
    }

    /**
     * gets the average occupancy rate pf a given ship
     * @param fleetId the fleet manager id
     * @param shipMmsi the ship's mmsi code
     * @param startDate starting date of the requested period
     * @param endDate ending date of the requested period
     * @return returns the average occupancy rate of the requested ship
     */
    public double getAverageOccupancyRate(int fleetId, int shipMmsi, String startDate, String endDate){
        try{
            return cargoManifestStore.getAverageOccupancyRate(mainStorage.getDatabaseConnection(), fleetId, shipMmsi, startDate, endDate);
        }catch (SQLException throwables){
            throwables.printStackTrace();
            return 0;
        }
    }
}
