package lapr.project.controller;

import lapr.project.data.MainStorage;
import lapr.project.model.Location;
import lapr.project.model.Storage;
import lapr.project.store.PortsGraph;

import java.util.LinkedList;
import java.util.List;

public class ShortestPathController {

    /**
     * The current port graph
     */
    private final PortsGraph portsGraph;

    /**
     * Calls the creator with the current storage instance
     */
    public ShortestPathController(){this(MainStorage.getInstance());}

    /**
     * Creates an instance of the controller with the current storage instance
     * @param storage The storage instance used to store all information
     */
    public ShortestPathController(MainStorage storage){this.portsGraph = storage.getPortsGraph();}

    /**
     * This method gets the shortest path from start to end, passing by N places
     * @param places The locals to pass by
     * @param start The starting location
     * @param end The ending location
     * @return Returns the shortest path from start to end, passing by N places
     */
    public LinkedList<Location> getShortestPathN(List<Location> places, Location start, Location end){
        return portsGraph.shortestPathN(places,start,end);
    }

    /**
     * This method gets the shortest path from start to end.
     *
     * @param start The starting location
     * @param end   The ending location
     * @return      Returns the shortest path from start to end
     */
    public LinkedList<Location> landOrSeaPath(Location start, Location end){
        return portsGraph.landOrSeaPath(start,end);
    }

    /**
     * This method gets the shortest maritime path from a starting point to an end point.
     * The path will only contain Storages(Ports).
     *
     * @param start The starting location
     * @param end   The ending location
     * @return      Returns the shortest maritime path from start to end
     */
    public LinkedList<Location> shortestMaritimePath(Storage start, Storage end) {
        return portsGraph.shortestMaritimePath(start, end);
    }

    /**
     * This method gets the shortest land path from a starting point to an end point.
     * The path can start/end in a Storage(Port) but will only go through Countries after that,
     * so if the starting/ending point is a Storage it must have a connection to a Country
     * Location or the path will be null.
     *
     * @param start The starting location
     * @param end   The ending location
     * @return      Returns the shortest land path from start to end
     */
    public LinkedList<Location> shortestLandPath(Location start, Location end) {
        return portsGraph.shortestLandPath(start, end);
    }

    /**
     * Uses the list of points that form the path to calculate its distance
     * @param path The path
     * @return returns the value of the path's distance
     */
    public double getPathDistance(LinkedList<Location> path){
        return portsGraph.getPathDistance(path);
    }
}
