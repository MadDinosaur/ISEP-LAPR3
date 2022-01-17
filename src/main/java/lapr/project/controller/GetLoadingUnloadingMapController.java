package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import lapr.project.data.StorageSqlStore;

import java.sql.Timestamp;
import java.util.List;

public class GetLoadingUnloadingMapController {
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
    public GetLoadingUnloadingMapController() {
        this.mainStorage = MainStorage.getInstance();
        this.storageSqlStore = new StorageSqlStore();
    }

    /**
     * Creates a instance of the controller with the current storage instance
     * @param storage the storage instance used to store all information
     * @param storageSqlStore the sql storage store
     */
    public GetLoadingUnloadingMapController(MainStorage storage, StorageSqlStore storageSqlStore) {
        this.mainStorage = storage;
        this.storageSqlStore = storageSqlStore;
    }

    /**
     * Gets the loading and unloading info of all manifests managed by the user during the week following a given date.
     *
     * @param portManagerId      the system user registration code
     * @param date               the timestamp to lookup in the format YYYY-MM-DD HH:MM:SS
     * @return a list of String composed of manifests date and respective vehicle and container details
     */
    public List<String> getLoadingUnloadingMap(String portManagerId, String date) {
        DatabaseConnection dbconnection = mainStorage.getDatabaseConnection();

        return storageSqlStore.getLoadingUnloadingMap(dbconnection, portManagerId, date);
    }

    /**
     * Generates a text view of the loading/unloading map for all storages of a given user:
     * Port
     * Operation Type
     * Load/Unload Date
     * Vehicle
     * Vehicle Id
     * No. of Container to Load/Unload
     * Container No.
     * Container Position
     * @param list of manifest information returned by getLoadingUnloadingMap(portManagerId, date)
     * @return the loading/unloading map of the storages
     */
    public String getLoadingUnloadingMapToString(List<String> list) {
        if (list == null || list.isEmpty()) return "Unable to generate map.";

        StringBuilder sb = new StringBuilder();
        sb.append("Port ID | Operation Type | Load/Unload Date | Vehicle | ID | No. of Container to Load/Unload | Container No. | Container Position");
        for (String s : list)
            sb.append(s).append('\n');
        return sb.toString();
    }
}