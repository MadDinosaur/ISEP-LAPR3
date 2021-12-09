package lapr.project.store;

import lapr.project.exception.IllegalLocationException;
import lapr.project.model.Location;
import lapr.project.model.graph.matrix.MatrixGraph;

public class PortsGraph {

    private final MatrixGraph<Location, Integer> mg;

    public PortsGraph(){
        mg = new MatrixGraph<>(false);
    }

    public boolean insertLocation(Location location){
        return mg.addVertex(location);
    }

    public boolean insertPath(Location from, Location to, int dist){
        return mg.addEdge(from, to, dist);
    }


}
