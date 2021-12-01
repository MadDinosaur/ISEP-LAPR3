package lapr.project.controller;

import lapr.project.data.CargoManifestSqlStore;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;

import java.sql.SQLException;
import java.sql.Timestamp;

public class GetOccupancyRateGivenMomentController {
    /**
     * the current ship store
     */
    private final DatabaseConnection databaseConnection;

    /**
     * Calls the creator with a the current storage instance
     */
    public GetOccupancyRateGivenMomentController(){
        this(MainStorage.getInstance());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     *
     * @param mainStorage the storage instance used to store all information
     */
    public GetOccupancyRateGivenMomentController(MainStorage mainStorage){
        this.databaseConnection = mainStorage.getDatabaseConnection();
    }

    /**
     * Searches the database for a desired ship to know the occupancy rate at a given moment
     * @param mmsi the ship's mmsi
     * @param givenMoment the moment to search for
     * @return the occupancy rate of the desired ship and moment
     */
    public double getOccupancyRateGivenMoment(int mmsi, Timestamp givenMoment){
        try{
            return CargoManifestSqlStore.getOccupancyRateGivenMoment(databaseConnection, mmsi, givenMoment);
        }catch (SQLException throwables){
            throwables.printStackTrace();
            return 0;
        }
    }
}
