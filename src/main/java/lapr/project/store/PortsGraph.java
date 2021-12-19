package lapr.project.store;

import lapr.project.model.Country;
import lapr.project.model.Location;
import lapr.project.model.Storage;
import lapr.project.model.graph.matrix.MatrixGraph;
import oracle.ucp.util.Pair;

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

        for (int i = 0; i < vertices; i++)
        {
            Location loc = mg.vertex(i);
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

                result[i] = color;


                countryIntegerMap.put((Country) loc, result[i]);
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

    /**
     * set's up the graph by connecting each point to itself and every point to the closes port in another country
     * @param n the number of closest port to add
     */
    public void setUpGraph(int n){
            
        List<Location> storages = new ArrayList<>();
        
        for (Location location : mg.vertices()){
            if (location instanceof Storage) {
                storages.add(location);
            }
            mg.addEdge(location, location, (double) 0);
        }

        for (Location location1 : storages){
            List<Pair<Location, Double>> closest = new ArrayList<>();
            for (Location location2 : storages){
                if(location1.getCountry().equals(location2.getCountry())){
                    mg.addEdge(location1, location2, location1.distanceBetween(location2));
                } else{
                    closest.add(new Pair<>(location2, location1.distanceBetween(location2)));
                }
            }

            if (n != 0) {
                closest.sort(Comparator.comparing(Pair::get2nd));

                int numEdges = Math.min(n, closest.size());

                for (int i = 0; i < numEdges; i++) {
                    mg.addEdge(location1, closest.get(i).get1st(), closest.get(i).get2nd());
                }
            }
        }
    }

    /**
     * returns the graph
     * @return returns a clone of graph
     */
    public MatrixGraph<Location, Double> getMg() {
        return mg.clone();
    }
}
