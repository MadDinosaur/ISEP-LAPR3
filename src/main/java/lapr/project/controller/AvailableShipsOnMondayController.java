package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import lapr.project.data.ShipSqlStore;
import oracle.ucp.util.Pair;

import java.util.List;

public class AvailableShipsOnMondayController {
    /**
     *  The current main store
     */
    private final MainStorage mainStorage;

    /**
     * Calls the creator with the current storage instance
     */
    public AvailableShipsOnMondayController() {
        this(MainStorage.getInstance());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     * @param storage the storage instance used to store all information
     */
    public AvailableShipsOnMondayController(MainStorage storage) {this.mainStorage = storage;}

    /**
     * Fetches the database for a list of available ships and their location for next week's monday
     *
     * @return a list of available ships and their respective location
     */
    public List<Pair<Integer, Integer>> getAvailableShipsOnMonday() {
        DatabaseConnection dbconnection = mainStorage.getDatabaseConnection();
        ShipSqlStore shipStore = new ShipSqlStore();

        return shipStore.getAvailableShipsOnMonday(dbconnection);
    }

    /**
     * Takes a list of available ships and respective location and creates a presentation String
     *
     * @param availableShipsList a list containing the ship's mmsi and the respective port's identification number
     * @return a string containing all the information
     */
    public  String AvailableShipsOnMondayToString(List<Pair<Integer, Integer>> availableShipsList) {
        if (availableShipsList == null)
            return "There are no available ships next monday.";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The available ports and their respective location next week's monday are: \n");

        int count = 1;

        for (Pair<Integer, Integer> pair : availableShipsList) {
            stringBuilder.append(count)
                    .append(". Ship's mmsi: ")
                    .append(pair.get1st())
                    .append(" Port's identification number (where it will be located): ")
                    .append(pair.get2nd())
                    .append("\n");

            count++;
        }

        return stringBuilder.toString();
    }
}
