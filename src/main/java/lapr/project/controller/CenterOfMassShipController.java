package lapr.project.controller;

import lapr.project.data.MainStorage;
import lapr.project.store.ShipStore;
import oracle.ucp.util.Pair;

public class CenterOfMassShipController {

    /**
     * The current ship Store
     */
    private final ShipStore shipStore;

    /***
     * Calls the creator with the current storage instance
     */
    public CenterOfMassShipController(){
        this(MainStorage.getInstance());
    }

    /**
     * Creates an instance of the controller with the current storage instance
     * @param storage The storage instance used to store all information
     */
    public CenterOfMassShipController(MainStorage storage){
        this.shipStore = storage.getShipStore();
    }

    public Pair<Double, Double> getCenterOfMass(Double mass, Double length,Double width, Double massTower, Pair<Double,Double> tower){
        return shipStore.getCenterOfMass(mass, length, width, massTower, tower);
    }
}
