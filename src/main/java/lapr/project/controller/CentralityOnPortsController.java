package lapr.project.controller;

import lapr.project.data.MainStorage;
import lapr.project.model.Storage;
import lapr.project.store.PortsGraph;
import oracle.ucp.util.Pair;

import java.util.List;

public class CentralityOnPortsController {

    /**
     *  The current ship store
     */
    private final PortsGraph portsGraph;

    /**
     * Calls the creator with the current storage instance
     */
    public CentralityOnPortsController(){
        this(MainStorage.getInstance());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     * @param storage the storage instance used to store all information
     */
    public CentralityOnPortsController(MainStorage storage) {
        this.portsGraph = storage.getPortsGraph();
    }

    /**
     * obtains the ports with highest centrality level
     * @param n number of ports the user wants to see
     * @return returns ports with highest centrality level
     */
    public List<Pair<Storage, Integer>> getCentrality(int n){
        return portsGraph.getCriticalPorts(n);
    }
}
