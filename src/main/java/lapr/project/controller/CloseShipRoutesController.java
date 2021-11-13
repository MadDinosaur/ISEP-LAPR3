package lapr.project.controller;

import lapr.project.data.MainStorage;
import lapr.project.model.Ship;
import lapr.project.store.ShipStore;
import lapr.project.utils.SorterTraveledDistance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class CloseShipRoutesController {

    /**
     *  The current ship store
     */
    private final ShipStore shipStore;

    /**
     * Calls the creator with the current storage instance
     */
    public CloseShipRoutesController() {
        this(MainStorage.getInstance());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     * @param storage the storage instance used to store all information
     */
    public CloseShipRoutesController(MainStorage storage) {
        this.shipStore = storage.getShipStore();
    }

    /**
     * Gets pairs of ships with close coordinates, ordered by 1st first MMSI (ascending) and Traveled Distance (descending)
     * @return returns the sorted pairs of ships
     */
    public String getCloseShipRoutes() {

        int minTraveledDistance = 10;
        int distance = 5;

        HashMap<Ship, TreeSet<Ship>> routes = shipStore.getCloseShipRoutes(minTraveledDistance, distance);

        return shipStore.shipsPairedCloseRoutesToString(routes);
    }

}
