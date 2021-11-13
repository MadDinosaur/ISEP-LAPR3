package lapr.project.controller;


import lapr.project.data.MainStorage;
import lapr.project.model.Ship;
import lapr.project.store.ShipStore;
import oracle.ucp.util.Pair;

import java.util.*;

public class NTopShipsController {
    /**
     * the current ship store
     */
    private final ShipStore shipStore;

    /**
     * Calls the creator with the current storage instance
     */
    public NTopShipsController(){
        this(MainStorage.getInstance());
    }

    /**
     * Creates an instance of the controller with a given storage instance
     *
     * @param mainStorage the storage instance used to store all information
     */
    public NTopShipsController(MainStorage mainStorage){
        this.shipStore = mainStorage.getShipStore();
    }

    /**
     * Will return a List of strings containing the top n ships grouped by Vessel Type in a given time frame.
     * Each Vessel Type will contain 2 tops, one ordered by travelled distance and the other one by Mean SOG.
     *
     * @param n number of the top
     * @param date1 initial date of the time frame
     * @param date2 end date of the time frame
     * @return an ArrayList of Strings
     */
    public ArrayList<String> getTopNShips(int n, Date date1, Date date2) {
        HashMap<Integer, Pair<LinkedHashMap<Ship, Float>, LinkedHashMap<Ship, Double>>> orderedMaps = new HashMap<>();

        shipStore.getOrderedShipsGroupedByVesselType(date1, date2, orderedMaps);

        return shipStore.getTopNShipsToString(n, date1, date2, orderedMaps);
    }
}
