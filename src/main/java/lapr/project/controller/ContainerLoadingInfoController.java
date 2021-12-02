package lapr.project.controller;

import lapr.project.data.ContainerSqlStore;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import lapr.project.data.ShipSqlStore;
import lapr.project.mappers.dto.ShipDTO;
import lapr.project.model.Ship;
import lapr.project.model.Storage;
import lapr.project.store.StorageStore;
import oracle.ucp.util.Pair;

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
     * @return a String matrix, where first row are headers and next rows are the respective values
     */
    private List<List<String>> getNextUnloadingManifest(String captainId) {
        DatabaseConnection dbconnection = mainStorage.getDatabaseConnection();

        ContainerSqlStore containerStore = new ContainerSqlStore();
        ShipSqlStore shipStore = new ShipSqlStore();
        StorageStore storageStore = mainStorage.getStorageStore();

        Ship ship = shipStore.getShipByCaptainId(dbconnection, captainId);
        Storage port = storageStore.searchClosestStorage(ship.getPositioningDataList().arrivalCoordinates());

        return containerStore.getNextUnloadingManifest(dbconnection, ship.getMmsi(), port.getIdentification());
    }

    /**
     * Creates a table with list of containers to be offloaded in the next port,
     * including container identifier, type, position, and load.
     * The first line of the table is comprised of headers, remaining rows are container information.
     * @param captainId the ship's captain identification
     * @param portId the next port identification
     * @return a String matrix, where first row are headers and next rows are the respective values
     */
    private List<List<String>> getNextUnloadingManifest(String captainId, int portId) {
        DatabaseConnection dbconnection = mainStorage.getDatabaseConnection();

        ContainerSqlStore containerStore = new ContainerSqlStore();
        ShipSqlStore shipStore = new ShipSqlStore();

        Ship ship = shipStore.getShipByCaptainId(dbconnection, captainId);

        return containerStore.getNextUnloadingManifest(dbconnection, ship.getMmsi(), portId);
    }

    /**
     * Generates a text view of the containers to be offloaded in the next port:
     * Container Identifier
     * Type
     * Position
     * Load
     * @param captainId the ship's captain identification
     * @return the list of containers
     */
    public String getNextUnloadingManifestToString(String captainId) {
        List<List<String>> list = getNextUnloadingManifest(captainId);

        StringBuilder string = new StringBuilder();

        if (list.isEmpty()) string.append("No containers found.");

        for (List<String> row: list) {
            for (String cell : row)
                string.append(cell + "  ");
            string.append("\n");
        }
        return string.toString();
    }

    /**
     * Generates a text view of the containers to be offloaded in the next port:
     * Container Identifier
     * Type
     * Position
     * Load
     * @param captainId the ship's captain identification
     * @return the list of containers
     */
    public String getNextUnloadingManifestToString(String captainId, int portId) {
        List<List<String>> list = getNextUnloadingManifest(captainId, portId);

        StringBuilder string = new StringBuilder();

        if (list.isEmpty()) string.append("No containers found.");

        for (List<String> row: list) {
            for (String cell : row)
                string.append(cell + "  ");
            string.append("\n");
        }
        return string.toString();
    }
}