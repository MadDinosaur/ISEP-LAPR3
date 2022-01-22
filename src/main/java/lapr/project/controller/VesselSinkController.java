package lapr.project.controller;


import lapr.project.data.MainStorage;
import lapr.project.store.ShipStore;

import java.util.HashMap;

public class VesselSinkController {

    /**
     * The current ship Store
     */
    private final ShipStore shipStore;

    /***
     * Calls the creator with the current storage instance
     */
    public VesselSinkController(){
        this(MainStorage.getInstance());
    }

    /**
     * Creates an instance of the controller with the current storage instance
     * @param storage The storage instance used to store all information
     */
    public VesselSinkController(MainStorage storage){
        this.shipStore = storage.getShipStore();
    }

    /**
     * This method returns how much the vessel sunk, the total mass placed and the pressure exerted
     * @param mass The ship's mass
     * @param length The ship's length
     * @param width The ship's width
     * @param nContainers The number of containers in it
     * @return Returns a map with 3 informations: How much the vessel sunk, the total mass placed and the pressure exerted
     */
    public HashMap<String, Double> vesselSink(Double mass,Double length,Double width, int nContainers){
        return shipStore.vesselSink(mass,length,width,nContainers);
    }
}

