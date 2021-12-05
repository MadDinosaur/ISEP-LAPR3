package lapr.project.controller;

import lapr.project.data.ContainerSqlStore;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import lapr.project.data.ShipSqlStore;
import lapr.project.model.Ship;
import lapr.project.model.Storage;
import lapr.project.store.StorageStore;

import java.util.List;

public class ContainerLoadingInfoController {
    /**
     *  The current ship store
     */
    private final MainStorage mainStorage;

    /**
     * Calls the creator with the current storage instance
     */
    public ContainerLoadingInfoController() {
        this(MainStorage.getInstance());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     * @param storage the storage instance used to store all information
     */
    public ContainerLoadingInfoController(MainStorage storage) {this.mainStorage = storage;}

    /**
     * Creates a table with list of containers to be offloaded in the next port,
     * including container identifier, type, position, and load.
     * The "next port" is the closest port to the ship's current location.
     * @param captainId the ship's captain identification
     * @param loading flag to indicate a loading (true) or offloading (false) operation
     * @return a String matrix, where first row are headers and next rows are the respective values
     */
    private List<List<String>> getNextContainerManifest(String captainId, boolean loading) {
        DatabaseConnection dbconnection = mainStorage.getDatabaseConnection();

        ContainerSqlStore containerStore = new ContainerSqlStore();
        ShipSqlStore shipStore = new ShipSqlStore();
        StorageStore storageStore = mainStorage.getStorageStore();

        Ship ship = shipStore.getShipByCaptainId(dbconnection, captainId);
        Storage port = storageStore.searchClosestStorage(ship.getPositioningDataList().arrivalCoordinates());

        return containerStore.getContainerManifest(dbconnection, ship.getMmsi(), port.getIdentification(), loading);
    }

    /**
     * Creates a table with list of containers to be offloaded in the next port,
     * including container identifier, type, position, and load.
     * The first line of the table is comprised of headers, remaining rows are container information.
     * @param captainId the ship's captain identification
     * @param portId the next port identification
     * @param loading flag to indicate a loading (true) or offloading (false) operation
     * @return a String matrix, where first row are headers and next rows are the respective values
     */
    public List<List<String>> getNextContainerManifest(String captainId, int portId, boolean loading) {
        DatabaseConnection dbconnection = mainStorage.getDatabaseConnection();

        ContainerSqlStore containerStore = new ContainerSqlStore();
        ShipSqlStore shipStore = new ShipSqlStore();

        Ship ship = shipStore.getShipByCaptainId(dbconnection, captainId);

        return containerStore.getContainerManifest(dbconnection, ship.getMmsi(), portId, loading);
    }

     /**
     * Generates a text view of the containers to be offloaded in the next port:
     * Container Identifier
     * Type
     * Position
     * Load
     * @param list container information list returned by getNextContainerManifest
     * @return the list of containers
     */
    public String getNextContainerManifestToString(List<List<String>> list) {
        StringBuilder string = new StringBuilder();

        if (list == null || list.size() < 2) return "No containers found.";

        for (List<String> row: list) {
            for (String cell : row)
                string.append(cell + "  ");
            string.append("\n");
        }
        return string.toString();
    }
}