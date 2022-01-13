package lapr.project.controller;

import lapr.project.data.MainStorage;
import lapr.project.mappers.CountryMapper;
import lapr.project.mappers.dto.CountryDTO;
import lapr.project.model.Location;
import lapr.project.model.graph.matrix.MatrixGraph;
import lapr.project.store.PortsGraph;

import java.util.LinkedList;
import java.util.Map;

public class LongestCycleController {

    /**
     *  The current ship store
     */
    private final PortsGraph portsGraph;

    /**
     * Calls the creator with the current storage instance
     */
    public LongestCycleController() {
        this(MainStorage.getInstance());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     * @param storage the storage instance used to store all information
     */
    public LongestCycleController(MainStorage storage) {
        this.portsGraph = storage.getPortsGraph();
    }

    /**
     * returns the given location's biggest cycle in the graph
     * @return returns the given location's biggest cycle in the graph
     */
    public LinkedList<Location> getLongestCycle(Location location){
        return portsGraph.getBiggestCircuit(location);
    }

    /**
     * Uses the list of points that form the cycle to calculate it's distance
     * @param path the cycle
     * @return returns the value of the cycle's distance
     */
    public double getCycleDistance(LinkedList<Location> path){
        return portsGraph.getPathDistance(path);
    }
}
