package lapr.project.controller;


import lapr.project.data.MainStorage;
import lapr.project.model.Ship;
import lapr.project.store.ShipStore;
import oracle.ucp.util.Pair;

import java.util.*;

public class NTopShipsController {
    private final ShipStore shipStore;

    public NTopShipsController(){
        this(MainStorage.getInstance());
    }

    public NTopShipsController(MainStorage mainStorage){
        this.shipStore = mainStorage.getShipStore();
    }

    public ArrayList<String> getTopNShips(int n, Date date1, Date date2) {
        HashMap<Integer, Pair<LinkedHashMap<Ship, Float>, LinkedHashMap<Ship, Double>>> orderedMaps = new HashMap<>();

        shipStore.getOrderedShipsGroupedByVesselType(date1, date2, orderedMaps);

        return shipStore.getTopNShipsToString(n, date1, date2, orderedMaps);
    }
}
