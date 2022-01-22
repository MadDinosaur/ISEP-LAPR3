package lapr.project.controller;

import lapr.project.data.ContainerSqlStore;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;


import java.util.List;

public class ContainerAuditController {
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
    public ContainerAuditController() {
        this.mainStorage = MainStorage.getInstance();
        containerStore = new ContainerSqlStore();
    }

    /**
     * Creates a instance of the controller with the current sql stores (used for testing with mock DB connection classes)
     * @param containerStore the sql container store
     */
    public ContainerAuditController(MainStorage storage, ContainerSqlStore containerStore) {
        this.mainStorage = storage;
        this.containerStore = containerStore;
    }


    /**
     * Creates a table with list of operations performed on a given container of a given cargo manifest,
     * including user id, timestamp, operation type, container number and cargo manifest id.
     * @param containerNum the container number
     * @param cargoManifestId the cargo manifest id
     * @return a String matrix, where first row are headers and next rows are the respective values
     */
    public List<List<String>> getContainerAudit (int containerNum, int cargoManifestId) {
        DatabaseConnection dbconnection = mainStorage.getDatabaseConnection();

        return containerStore.getContainerAudit(dbconnection, containerNum, cargoManifestId);
    }

     /**
     * Generates a text view of the operations performed on a given container:
     * User Id
     * Timestamp
     * Operation Type
     * Container Number
      * Cargo Manifest Id
     * @param list container information list returned by getContainerAudit
     * @return the list of operations
     */
    public String getContainerAuditToString (List<List<String>> list) {
        StringBuilder string = new StringBuilder();

        if (list == null || list.size() < 2) return "No operations found.";

        for (List<String> row: list) {
            for (String cell : row)
                string.append(cell + "  ");
            string.append("\n");
        }
        return string.toString();
    }
}