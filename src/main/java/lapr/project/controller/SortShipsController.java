package lapr.project.controller;

import lapr.project.data.MainStorage;
import lapr.project.model.Ship;
import lapr.project.store.ShipStore;
import lapr.project.utils.ShipSorter;

import java.util.TreeMap;
import java.util.TreeSet;

public class SortShipsController {

    /**
     *  The current ship store
     */
    private final ShipStore shipStore;

    /**
     * Calls the creator with the current storage instance
     */
    public SortShipsController() {
        this(MainStorage.getInstance());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     * @param storage the storage instance used to store all information
     */
    public SortShipsController(MainStorage storage) {
        this.shipStore = storage.getShipStore();
    }

    /**
     * Gets the TreeMap of the ships all ordered by Traveled Distance and Number of Movements
     * @return returns the ships sorted
     */
    public TreeSet<String> sortShips() {
        ShipSorter sorter = new ShipSorter();

        TreeSet<Ship> shipsSorted = shipStore.sortShips(sorter);

        return shipStore.shipsSortedTraveledDistanceToString(shipsSorted);
    }




}
