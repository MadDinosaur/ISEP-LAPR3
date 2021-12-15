package lapr.project.controller;

import lapr.project.data.ContainerSqlStore;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;

import java.util.ArrayList;
import java.util.List;

public class ContainerRouteController {
    /**
     *  The current ship store
     */
    private final MainStorage mainStorage;
    /**
     * The current container sql store
     */
    private final ContainerSqlStore containerStore;

    /**
     * Calls the creator with the current storage instance
     */
    public ContainerRouteController() {
        this.mainStorage = MainStorage.getInstance();
        containerStore = new ContainerSqlStore();
    }

    /**
     * Creates a instance of the controller with the current sql stores (used for testing with mock DB connection classes)
     * @param containerStore the sql container store
     */
    public ContainerRouteController(MainStorage storage, ContainerSqlStore containerStore) {
        this.mainStorage = storage;
        this.containerStore = containerStore;
    }


    /**
     * Returns a given container's path from source to current location indicating time of arrival and
     * departure at each location and mean of transport (ship or truck) between
     * each pair of locations.
     * @param clientId the requesting client registration code
     * @param containerNum the container number
     * @return a String matrix, where first row are headers and next rows are the respective values
     */
    public List<List<String>> getContainerRoute (String clientId, int containerNum) {
        DatabaseConnection dbconnection = mainStorage.getDatabaseConnection();

        int shipmentId;
        if ((shipmentId = containerStore.getContainerShipment(dbconnection, clientId, containerNum)) == -1)
            return new ArrayList<>();

        return containerStore.getContainerRoute(dbconnection, shipmentId, containerNum);
    }

     /**
     * Generates a text view of a container's route:
     * Location
     * Operation
     * Mean of Transport
     * Timestamp
     * @param list container information list returned by getContainerRoute
     * @return the list of operations
     */
    public String getContainerRouteToString (List<List<String>> list) {
        StringBuilder string = new StringBuilder();

        if (list == null || list.size() < 2) return "Container is not in transit.";

        for (List<String> row: list) {
            for (String cell : row)
                string.append(cell + "  ");
            string.append("\n");
        }
        return string.toString();
    }
}