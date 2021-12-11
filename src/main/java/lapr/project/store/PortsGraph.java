package lapr.project.store;

import lapr.project.model.Country;
import lapr.project.model.Location;
import lapr.project.model.Storage;
import lapr.project.model.graph.matrix.MatrixGraph;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class PortsGraph {

    /**
     * the stored graph
     */
    private final MatrixGraph<Location, Double> mg;

    /**
     * starts the graph
     */
    public PortsGraph(){
        mg = new MatrixGraph<>(false);
    }

    /**
     * insert a new vertex to the graph
     * @param location the location being added
     */
    public void insertLocation(Location location){
        mg.addVertex(location);
    }

    /**
     * insert's a path to the graph
     * @param from the origin vertex
     * @param to the destination vertex
     * @param dist the distance between the locations
     */
    public void insertPath(Location from, Location to, Double dist){
        mg.addEdge(from, to, dist);
    }

    /**
     * insert's the port's path
     * @param from the identification of the starting storage
     * @param to the identification of the finishing storage
     * @param dist the distance between the ports
     */
    public void insertPortPath(int from, int to, double dist){
        Location country1 = null, country2 = null;

        for (Location location : mg.vertices()){
            if (location instanceof Storage) {
                Storage storage = (Storage) location;
                if (storage.getIdentification() == from)
                    country1 = location;
                if (storage.getIdentification() == to)
                    country2 = location;
            }
        }

        if (country1 != null && country2 != null) {
            mg.addEdge(country1, country2, dist);
        }

    }

    /**
     * inserts connection between the countries capitals
     * @param from the origin country
     * @param to the destiny country
     */
    public void insertCountryPath(String from, String to){
        Location country1 = null, country2 = null;

        for (Location location : mg.vertices()){
            if (location instanceof Country) {
                if (location.getCountry().equals(from))
                    country1 = location;
                if (location.getCountry().equals(to))
                    country2 = location;
            }
        }

        if (country1 != null && country2 != null)
            mg.addEdge(country1, country2, country1.distanceBetween(country2));
    }

//    public Map<Country, Integer> greedyColoring()
//    {
//        MatrixGraph<Location, Double> countryMg =  mg.clone();
//
//        for (Location location : countryMg.vertices())
//            if (location instanceof Storage)
//                countryMg.removeVertex(location);
//
//        int V = countryMg.numVertices();
//
//        int[] result = new int[V];
//        Arrays.fill(result, -1);
//
//        result[0] = 0;
//
//        boolean[] available = new boolean[V];
//        Arrays.fill(available, true);
//
//        for (int u = 1; u < V; u++)
//        {
//            for (Location location : countryMg.adjVertices(countryMg.vertex(u))) {
//                int i = countryMg.key(location);
//                if (result[i] != -1)
//                    available[result[i]] = false;
//            }
//
//            int cr;
//            for (cr = 0; cr < V; cr++){
//                if (available[cr])
//                    break;
//            }
//
//            result[u] = cr;
//
//            Arrays.fill(available, true);
//        }
//
//        for (int u = 0; u < V; u++)
//            System.out.println("Vertex " + countryMg.vertex(u).getCountry() + " --->  Color "
//                    + result[u]);
//        return null;
//    }

    /**
     * returns the graph
     * @return returns a clone of graph
     */
    public MatrixGraph<Location, Double> getMg() {
        return mg.clone();
    }
}
