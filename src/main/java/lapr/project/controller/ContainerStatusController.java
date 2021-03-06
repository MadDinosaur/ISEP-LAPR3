package lapr.project.controller;

import lapr.project.data.ContainerSqlStore;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import oracle.ucp.util.Pair;

import java.util.List;

public class ContainerStatusController {
    /**
     *  The current ship store
     */
    private final MainStorage mainStorage;
    /**
     * The current container store
     */
    private final ContainerSqlStore containerStore;

    /**
     * Calls the creator with the current storage instance
     */
    public ContainerStatusController() {
        this.mainStorage = MainStorage.getInstance();
        this.containerStore = new ContainerSqlStore();
    }

    /**
     * Creates a instance of the controller with the current storage instance
     * @param storage the storage instance used to store all information
     * @param containerStore the sql container store
     */
    public ContainerStatusController(MainStorage storage, ContainerSqlStore containerStore) {
        this.mainStorage = storage;
        this.containerStore = containerStore;
    }

    /**
     * Creates a table with a the status of a given container:
     * Container Identifier
     * Type of Location
     * Name of Location
     * @param containerNum the container identification
     * @return a list of Pair<String, String>, where column headers are paired with their respective value
     */
    public List<String> getContainerStatus(int containerNum) {
        DatabaseConnection dbconnection = mainStorage.getDatabaseConnection();

        return containerStore.getContainerStatus(dbconnection, containerNum);
    }

    /**
     * Generates a text view of the status of a given container:
     * Container Identifier
     * Type of Location
     * Name of Location
     * @param list of container information returned by getContainerStatus(containerNum)
     * @return the status of the container
     */
    public String getContainerStatusToString(List<String> list) {
        if (list.isEmpty()) return "Container not found.";
        if (list.size() != 3) return "Input parameters are incorrect. No containers can be searched.";

        return String.format("Container no. %s is currently in %s %s", list.get(0), list.get(1), list.get(2));
    }
}