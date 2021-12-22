package lapr.project.controller;

import oracle.ucp.util.Pair;

import lapr.project.data.MainStorage;
import lapr.project.data.StorageSqlStore;

import java.sql.SQLException;

public class WarehouseOccupancyAndEstimateController {
    /**
     * The current storage
     */
    private final MainStorage mainStorage;

    /**
     * The current sql storage store
     */
    private final StorageSqlStore storageStore;

    /**
     * Calls the creator with a the current storage instance
     */
    public WarehouseOccupancyAndEstimateController() {
        this.mainStorage = MainStorage.getInstance();
        this.storageStore = new StorageSqlStore();
    }

    /**
     * Creates a instance of the controller with the current storage instance and sql store
     *
     * @param mainStorage the storage instance used to store all information
     * @param storageSqlStore the store instance used to store database requests
     */
    public WarehouseOccupancyAndEstimateController(MainStorage mainStorage, StorageSqlStore storageSqlStore) {
        this.mainStorage = mainStorage;
        this.storageStore = storageSqlStore;
    }

    public Pair<Integer,Double> getOccupancyRate(int storageId){
        try {
            return storageStore.getOccupancyRate(mainStorage.getDatabaseConnection(), storageId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public Pair<Integer,Integer> getEstimateLeavingContainers30Days(int storageId){
        try {
            return storageStore.getEstimateLeavingContainers30Days(mainStorage.getDatabaseConnection(), storageId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
