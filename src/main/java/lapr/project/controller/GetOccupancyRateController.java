package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;

public class GetOccupancyRateController {
    /**
     * the current ship store
     */
    private final DatabaseConnection databaseConnection;

    /**
     * Calls the creator with a the current storage instance
     */
    public GetOccupancyRateController(){
        this(MainStorage.getInstance());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     *
     * @param mainStorage the storage instance used to store all information
     */
    public GetOccupancyRateController(MainStorage mainStorage){
        this.databaseConnection = mainStorage.getDatabaseConnection();
    }

    /**
     * gets the occupancy rate of a given ship and returns it
     * @param captain_id captain's ID
     * @param manifest_id Cargo Manifest ID
     * @return occupancy rate
     */
    public String getOccupancyRate(int captain_id, int manifest_id){
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
