package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import lapr.project.data.ShipSqlStore;


import java.util.List;

public class GetShipTripsThresholdController {
    /**
     *  The current ship store
     */
    private final MainStorage mainStorage;
    /**
     * The current Ship store
     */
    private final ShipSqlStore shipSqlStore;

    /**
     * Calls the creator with the current Ship sql instance
     */
    public GetShipTripsThresholdController() {
        this(MainStorage.getInstance(), new ShipSqlStore());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     * @param storage the storage instance used to store all information
     * @param shipSqlStore the sql ship store
     */
    private GetShipTripsThresholdController(MainStorage storage, ShipSqlStore shipSqlStore) {
        this.mainStorage = storage;
        this.shipSqlStore = shipSqlStore;
    }

    /**
     * returns a list of the trips found and their occupancy rate bellow the threshold
     * @param managerId the fleet's manager id
     * @return returns a list of the trips found and their occupancy rate bellow the threshold
     */
    public List<String> getTripsBeneathThreshold(int managerId) {
        DatabaseConnection connection = mainStorage.getDatabaseConnection();

        return shipSqlStore.getTripOccupancyRate(connection, managerId);
    }
}
