package lapr.project.controller;

import lapr.project.data.ContainerSqlStore;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import lapr.project.data.StorageSqlStore;

import java.util.List;

public class GetOccupancyMapController {
    /**
     *  The current ship store
     */
    private final MainStorage mainStorage;
    /**
     * The current storage store
     */
    private final StorageSqlStore storageSqlStore;

    /**
     * Calls the creator with the current storage instance
     */
    public GetOccupancyMapController() {
        this.mainStorage = MainStorage.getInstance();
        this.storageSqlStore = new StorageSqlStore();
    }

    /**
     * Creates a instance of the controller with the current storage instance
     * @param storage the storage instance used to store all information
     * @param storageSqlStore the sql storage store
     */
    public GetOccupancyMapController(MainStorage storage, StorageSqlStore storageSqlStore) {
        this.mainStorage = storage;
        this.storageSqlStore = storageSqlStore;
    }

    /**
     * Gets the occupancy rate of a given storage during a given month.
     * Returns only the first date of the month and dates when the occupancy rates suffered changes.
     * @param storageId          the storage identification
     * @param month              the month to lookup
     * @param year               the year to lookup
     * @return a list of String composed of a date and the respective occupancy rate percentage
     */
    public List<String> getOccupancyMap(int storageId, int month, int year) {
        DatabaseConnection dbconnection = mainStorage.getDatabaseConnection();

        return storageSqlStore.getOccupancyMap(dbconnection, storageId, month, year);
    }

    /**
     * Generates a text view of the occupancy map of a given storage:
     * Date
     * Occupancy Percentage
     * @param list of storage information returned by getOccupancyMap(storageId, month, year)
     * @return the occupancy map of the storage
     */
    public String getOccupancyMapToString(List<String> list) {
        if (list == null || list.isEmpty()) return "Storage not found.";

        StringBuilder sb = new StringBuilder();
        for (String s : list)
            sb.append(s + '\n');
        return sb.toString();
    }
}