package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import lapr.project.data.ShipSqlStore;

import java.util.List;


public class GetIdleDaysFleetController {

    /**
     *  The current main storage
     */
    private final MainStorage mainStorage;

    /**
     * The current Ship SQL Store
     */
    private final ShipSqlStore shipSqlStore;

    /**
     * Calls the creator with the current Ship SQL Instance
     */
    public GetIdleDaysFleetController(){
        this(MainStorage.getInstance(),new ShipSqlStore());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     * @param storage the storage instance used to store all information
     * @param shipSqlStore the sql ship store
     */
    public GetIdleDaysFleetController(MainStorage storage, ShipSqlStore shipSqlStore){
        this.mainStorage = storage;
        this.shipSqlStore =shipSqlStore;
    }

    /**
     * Returns a list with all the idle days for each ship
     * @param fleetManagerID the fleet's manager ID
     * @return Returns a list with all the idle days for each ship
     */
    public List<String> getIdleDays(int fleetManagerID){
        DatabaseConnection connection = mainStorage.getDatabaseConnection();

        return shipSqlStore.idleDaysFleet(connection,fleetManagerID);
    }
}
