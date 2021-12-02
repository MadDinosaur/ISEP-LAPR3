package lapr.project.controller;

import lapr.project.data.CargoManifestSqlStore;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;

import java.sql.SQLException;

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
     * @param shipMmsi captain's ID
     * @param manifestId Cargo Manifest ID
     * @return occupancy rate
     */
    public double getOccupancyRate(int shipMmsi, int manifestId){
        try{
            return CargoManifestSqlStore.getOccupancyRate(databaseConnection, shipMmsi, manifestId);
        }catch (SQLException throwables){
            throwables.printStackTrace();
            return 0;
        }
    }
}
