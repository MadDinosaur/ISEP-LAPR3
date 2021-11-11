package lapr.project.controller;

import jdk.internal.net.http.common.Pair;
import lapr.project.data.MainStorage;
import lapr.project.model.Ship;
import lapr.project.store.ShipStore;

import java.util.Date;
import java.util.HashMap;

public class NTopShipsController {
    private final ShipStore shipStore;

    public NTopShipsController(){
        this(MainStorage.getInstance());
    }

    public NTopShipsController(MainStorage mainStorage){
        this.shipStore = mainStorage.getShipStore();
    }

    public void getTopNShips(int n, Date date1, Date date2) {
        HashMap<Integer, HashMap<Integer, Pair<Ship, Float>>> topNShips = new HashMap<>();

        shipStore.getTopNShips(n, date1, date2, topNShips);
    }
}
