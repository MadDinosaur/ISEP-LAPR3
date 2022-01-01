package lapr.project.controller;

import oracle.ucp.util.Pair;

import lapr.project.data.MainStorage;
import lapr.project.data.StorageSqlStore;

import java.sql.SQLException;
import java.util.List;

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

    /**
     * Gets the storage identification number and it's occupancy rate
     * @param storageId the storage id
     * @return storage id and it's occupancy rate
     */
    public Pair<Integer,Double> getOccupancyRate(int storageId){
        try {
            return storageStore.getOccupancyRate(mainStorage.getDatabaseConnection(), storageId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the number of leaving containers in a space of 30 days
     * @param storageId the storage id
     * @return storage id with the number of leaving containers
     */
    public Pair<Integer,Integer> getEstimateLeavingContainers30Days(int storageId){
        try {
            return storageStore.getEstimateLeavingContainers30Days(mainStorage.getDatabaseConnection(), storageId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the id of the containers leaving the storage in 30 days
     * @param storageId the storage id
     * @return a list of leaving container id
     */
    public List<Integer> getContainers30Days(int storageId){
        try {
            return storageStore.getContainers30Days(mainStorage.getDatabaseConnection(), storageId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
