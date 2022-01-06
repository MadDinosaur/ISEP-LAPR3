package lapr.project.store;

import lapr.project.model.Country;
import lapr.project.model.Location;
import lapr.project.model.Storage;
import lapr.project.model.graph.Algorithms;
import lapr.project.model.graph.Edge;
import lapr.project.model.graph.matrix.MatrixGraph;
import oracle.ucp.util.Pair;

import java.lang.reflect.Array;
import java.util.*;

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
    public boolean insertPortPath(int from, int to, double dist){
        Location Storage1 = null, Storage2 = null;

        for (Location location : mg.vertices()){
            if (location instanceof Storage) {
                Storage storage = (Storage) location;
                if (storage.getIdentification() == from)
                    Storage1 = location;
                if (storage.getIdentification() == to)
                    Storage2 = location;
            }
        }

        if (Storage1 != null && Storage2 != null) {
            if (!Storage1.getCountry().equals(Storage2.getCountry()))
                return mg.addEdge(Storage1, Storage2, dist);
            else{
                mg.addEdge(Storage1, Storage2, dist);
                return false;
            }
        }
        return false;
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

    /**
     * returns a map with every country and their color
     * @return returns a map with every country and their color
     */
    public Map<Country, Integer> colourCountries() {
        Map<Country, Integer> countryIntegerMap = new HashMap<>();

        int vertices = mg.numVertices();

        if (vertices == 0)
            return countryIntegerMap;
        int[] result = new int[vertices];
        Arrays.fill(result, -1);

        boolean[] available = new boolean[vertices];
        Arrays.fill(available, true);

        List<Pair<Location, Integer>> countryList = new ArrayList<>();
        for (int i = 0; i < vertices; i++)
        {
            Location location = mg.vertex(i);
            countryList.add(new Pair<>(location, mg.adjVertices(location).size()));
        }

        countryList.sort(Collections.reverseOrder(Comparator.comparing(Pair::get2nd)));

        for (int i = 0; i < vertices; i++)
        {
            Location loc = countryList.get(i).get1st();

            if (loc instanceof Country) {
                for (Location location : mg.adjVertices(loc)) {
                    int j = mg.key(location);
                    if (result[j] != -1)
                        available[result[j]] = false;
                }

                int color;
                for (color = 0; color < vertices; color++) {
                    if (available[color])
                        break;
                }

                int key = mg.key(loc);

                result[key] = color;


                countryIntegerMap.put((Country) loc, result[key]);
                Arrays.fill(available, true);
            }
        }
        return countryIntegerMap;
    }

    /**
     * Creates a String with all of the graph countries colored and the color of it's bordering countries
     * @param color the map with all the countries colours
     * @return A String with all of the graph countries colored and the color of it's bordering countries
     */
    public String showColours(Map<Country, Integer> color){
        StringBuilder sb = new StringBuilder();
        for (Country country : color.keySet()){
            sb.append("Country : ").append(country.getCountry()).append(" ----> Colour ").append(color.get(country)).append("\n");
            List<Location> locations = (List<Location>) mg.adjVertices(country);
            sb.append("Bordering Countries :\n");
            for (Location location : locations) {
                if (location instanceof Country && !location.equals(country))
                    sb.append("Border Country : ").append(location.getCountry()).append(" ----> Colour ").append(color.get(location)).append("\n");
            }

            sb.append("\n\n");
        }
        return sb.toString();
    }

    /** Gets a list with locations of all places with the less distance
     * Overall Complexity: V*V
     *
     * @param minDistGraph The matrix of costs provided by the Floyd Warshall algorithm
     * @param size The number of vertices
     * @return returns a list with locations of all places with the less distance
     */
    private List<String> minDistanceLocation(MatrixGraph<Location,Double> minDistGraph,int size,int n){
        List<String> result = new ArrayList<>();                    // The result list
        List<Pair<Double,String>> temporary = new ArrayList<>();

        for(int i = 0; i < size;i++){                               // Each line...
            double sum = 0;                                         // ... has its own sum
            int numberSize = 0;

            for(int j = 0; j < size; j++){

                if(j != i){
                    Edge<Location, Double> edge = minDistGraph.edge(i,j);
                    if (edge != null) {
                        sum = sum + edge.getWeight(); // After adding all the weights of the line...
                        numberSize++;
                    }
                }
            }
            double average = Double.MAX_VALUE;
            if (numberSize != 0)
                average = sum / numberSize;                            // ... calculates the average of the line

            temporary.add(new Pair<>(average,minDistGraph.vertex(i).toString()));   // Introduces in a list a Pair with the average and the correspondent vertex
        }

        temporary.sort(Comparator.comparing(Pair::get1st));                     // Orders in crescent order by the average

        for(int k = 0; k < Math.min(n, size); k++){
            result.add(temporary.get(k).get2nd());
        }

        return  result;                                              // The list of indexes is returned
    }

    /** Gets a map for every continent and its correspondent closest places
     *
     * @param graphMap a map with every continent and its correspondent ports
     * @return returns a map for every continent and its correspondent closest places
     */
    public HashMap<String, List<String>> minDistanceByContinent(HashMap<String, PortsGraph> graphMap, int n){
        HashMap<String, List<String>> resultMap = new HashMap<>();                           // The result map to be returned

        for(String key : graphMap.keySet()){                                                  // For each key AKA Continent

            MatrixGraph<Location,Double> g = graphMap.get(key).getMg();                                        // Gets the respective graph

            MatrixGraph<Location,Double> minDistGraph = Algorithms.FloydWarshallAlgorithm(g,Double::compare,Double::sum);      // Gets the minDist graph using Floyd Warshall Algorithm

            resultMap.put(key,minDistanceLocation(minDistGraph,g.numVertices(),n));
        }
        return  resultMap;
    }

    /**
     * returns the graph
     * @return returns a clone of graph
     */
    public MatrixGraph<Location, Double> getMg() {
        return mg.clone();
    }
}
