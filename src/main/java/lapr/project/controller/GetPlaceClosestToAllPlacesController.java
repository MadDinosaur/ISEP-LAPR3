package lapr.project.controller;

import lapr.project.data.CountrySqlStore;
import lapr.project.data.MainStorage;
import lapr.project.store.PortsGraph;

import java.util.HashMap;
import java.util.List;

public class GetPlaceClosestToAllPlacesController {
    /**
     *  The current main store
     */
    private final MainStorage mainStorage;

    /**
     * Calls the creator with the current storage instance
     */
    public GetPlaceClosestToAllPlacesController() {
        this(MainStorage.getInstance());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     * @param storage the storage instance used to store all information
     */
    public GetPlaceClosestToAllPlacesController(MainStorage storage) {
        this.mainStorage = storage;
    }

    /**
     * returns a hashmap with the places closest to all other places grouped by continent
     * @return a hashmap with the places closest to all other places grouped by continent
     */
    public HashMap<String, List<String>> getPlaceClosestToAllPlaces(int n){
        CountrySqlStore sqlStore = new CountrySqlStore();
        PortsGraph pg = new PortsGraph();
        HashMap<String, PortsGraph> graphMap = sqlStore.loadGraphMapByContinent(mainStorage.getDatabaseConnection(), 10);

        return pg.minDistanceByContinent(graphMap,n);
    }
}
