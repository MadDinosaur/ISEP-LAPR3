package lapr.project.controller;

import lapr.project.data.ContainerSqlStore;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import lapr.project.store.ShipStore;

public class ContainerStatusController {
    /**
     *  The current ship store
     */
    private final MainStorage mainStorage;

    /**
     * Calls the creator with the current storage instance
     */
    public ContainerStatusController() {
        this(MainStorage.getInstance());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     * @param storage the storage instance used to store all information
     */
    public ContainerStatusController(MainStorage storage) {this.mainStorage = storage;}

    public String getContainerStatus(String clientId, int containerNum) {
        DatabaseConnection dbconnection = mainStorage.getDatabaseConnection();

        ContainerSqlStore containerStore = new ContainerSqlStore();
        if (containerStore.checkContainerInShipment(dbconnection, clientId, containerNum))
            return containerStore.getContainerStatus(dbconnection, containerNum);

        return "Unauthorized access. Container is not part of client's shipment.";
    }

}