package lapr.project.controller;

import lapr.project.data.ContainerSqlStore;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import lapr.project.exception.UnauthorizedOperationException;
import lapr.project.store.ShipStore;
import oracle.ucp.util.Pair;

import java.util.List;

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

    /**
     * Creates a table with a the status of a given container:
     * Container Identifier
     * Type of Location
     * Name of Location
     * @param containerNum the container identification
     * @return a list of Pair<String, String>, where column headers are paired with their respective value
     */
    private List<Pair<String, String>> getContainerStatus(int containerNum) {
        DatabaseConnection dbconnection = mainStorage.getDatabaseConnection();

        ContainerSqlStore containerStore = new ContainerSqlStore();
        return containerStore.getContainerStatus(dbconnection, containerNum);
    }

    /**
     * Generates a text view of the status of a given container:
     * Container Identifier
     * Type of Location
     * Name of Location
     * @param containerNum the container identification
     * @return the status of the container
     */
    public String getContainerStatusToString(int containerNum) {
        try {
            List<Pair<String, String>> list = getContainerStatus(containerNum);

            StringBuilder string = new StringBuilder();
            for (Pair<String, String> value: list)
                string.append(String.format("%s: %s ", value.get1st(), value.get2nd()));
            return string.toString();
        } catch (UnauthorizedOperationException e) {
            return e.getMessage();
        }
    }
}