package lapr.project.store;

import lapr.project.model.Country;
import lapr.project.model.Location;
import lapr.project.model.Storage;
import lapr.project.model.graph.Algorithms;
import lapr.project.model.graph.Edge;
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
            if (!Storage1.getCountry().equals(Storage2.getCountry())){
                mg.addEdge(Storage1, Storage2, dist);
                return true;
            }else{
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
     * After getting a list with the cycles that employ the chosen vertex this method chooses
     * the one that passes through most amount of vertex' and has the lowest travel distance
     * @param vert the chosen vertex
     * @return returns a linked
     */
    public LinkedList<Location> getBiggestCircuit(Location vert){
        ArrayList<LinkedList<Location>> paths = Algorithms.vertCycles(mg, vert, Double::compareTo);

        if (paths.isEmpty())
            return new LinkedList<>();

        ArrayList<LinkedList<Location>> Biggest = new ArrayList<>();
        int maxEdges = 0;

        for (LinkedList<Location> path: paths){
            if (path.size() > maxEdges){
                maxEdges = path.size();
                Biggest.clear();
                Biggest.add(path);
            } else if (path.size() == maxEdges){
                Biggest.add(path);
            }
        }

        double minDistance = Double.MAX_VALUE;
        LinkedList<Location> finalPath = new LinkedList<>();

        for (LinkedList<Location> path: Biggest){
            double distance = getPathDistance(path);

            if (distance < minDistance){
                finalPath = path;
                minDistance = distance;
            }
        }

        while(finalPath.get(0) != vert){
            finalPath.pop();
            finalPath.add(finalPath.get(0));
        }

        return finalPath;
    }

    /**
     * This method first gets all the permutations possible for the locations given and gets the shortest dist
     * to be used after by Dijkstra's Algorithm
     * @param places The locations to pass through
     * @param start The starting location
     * @param end The ending location
     * @return Returns the combination with less distance
     */
    private List<Location> lessDistancePath(List<Location> places, Location start, Location end){

        List<List<Location>> paths = Algorithms.generatePerm(places); /** Permutações dos pontos médios */

        MatrixGraph<Location, Double> g = getMg();
        MatrixGraph<Location, Double> minDistMatrix = Algorithms.FloydWarshallAlgorithm(g,Double::compare,Double::sum); /** Distancia minima entre todos os locais */
        List<Location> result = new ArrayList<>();
        double minDist = Double.MAX_VALUE;

        for(List<Location> list : paths){

            list.add(0,start);              // Adiciona o start ao inicio da lista
            list.add(end);                        // Adiciona o end ao final da lista
            double dist = 0;

            for( int i = 0; i < list.size()-1;i++){             // Percorre todos os valores da lista menos o último

                if(minDistMatrix.edge(list.get(i),list.get(i+1)) == null) {     // Pega na edge e vê se é null, se for null a lista atual de localizações fica logo fora de questão
                    dist = Double.MAX_VALUE;
                    break;
                }

                dist += minDistMatrix.edge(list.get(i), list.get(i + 1)).getWeight();   // Se não for null, soma-se a distancia dessa edge ao valor total da distância

            }

            if(dist < minDist){         // Verifica-se a distancia é menor que a atual menor distância
                minDist = dist;
                result = list;
            }
        }
        return result;
    }

    /**
     * This method gets the shortest path from start to end, passing by N places
     * @param places The locals to pass by
     * @param start The starting location
     * @param end The ending location
     * @return Returns the shortest path from start to end, passing by N places
     */
    public LinkedList<Location> shortestPathN(List<Location> places, Location start, Location end){

        MatrixGraph<Location, Double> g = getMg();
        LinkedList<Location> result = new LinkedList<>();
        List<Location> path = lessDistancePath(places,start,end);


        for(int i = 0; i < path.size()-1;i++){

            LinkedList<Location> tempPath = new LinkedList<>();
            Algorithms.shortestPath(g,path.get(i),path.get(i+1),Double::compare,Double::sum,0.0,tempPath);
            result.addAll(tempPath);
            if(i != path.size()-2) result.removeLast();

        }
        return result;
    }

    /**
     * obtains the ports with highest critical point
     * @param n number of ports the user wants to see
     * @return returns list of pairs where each pair contains a storage and the number of shortest paths that pass through it, in decreasing order
     */
    public List<Pair<Storage, Integer>> getCriticalPorts(int n){

        HashMap<Storage, Integer> result = new HashMap<>();
        ArrayList<LinkedList<Location>> paths = new ArrayList<>();

        for(Location place : mg.vertices()){
            ArrayList<LinkedList<Location>> path = new ArrayList<>();

            Algorithms.shortestPaths(mg,place,Double::compare, Double::sum, 0.0, path, new ArrayList<Double>());

            paths.addAll(path);
            if(place instanceof Storage){
                result.put((Storage) place,0);
            }
        }

        for(LinkedList<Location> list : paths){
            for(Location place : list){
                if(place instanceof Storage){
                    Integer integer = result.get(place);
                    result.put((Storage) place, integer  + 1);
                }
            }
        }


        List<Storage> keys = new ArrayList<Storage>(result.keySet());   //list with all keys
        List<Integer> values = new ArrayList<Integer>(result.values()); //list with all values
        List<Pair<Storage,Integer>> returnResult = new ArrayList<>();   //list of all pairs

        for(int i=0; i<keys.size(); i++){
            returnResult.add(new Pair<Storage, Integer>(keys.get(i),result.get(keys.get(i))));
        }

        returnResult.sort(Collections.reverseOrder(Comparator.comparing(Pair::get2nd)));                     // Orders in decreasing order by the average

        List<Pair<Storage, Integer>> centrality = new ArrayList<>();

        for(int i=0; i<Integer.min(n, returnResult.size()); i++){
            centrality.add(new Pair<Storage, Integer>(returnResult.get(i).get1st(), returnResult.get(i).get2nd()));
        }

        return centrality;
    }

    /**
     * Gets the shortest path between two locals, using the Dijkstra's Algorithm
     * @param start The starting location
     * @param end The ending location
     * @return Returns a list with the shortest path between two locals
     */
    public LinkedList<Location>  landOrSeaPath(Location start, Location end){
        LinkedList<Location> path = new LinkedList<>();
        Algorithms.shortestPath(getMg(),start,end,Double::compare,Double::sum,0.0,path);
        return path;
    }

    public LinkedList<Location> shortestLandPath(Location start, Location end) {
        MatrixGraph<Location, Double> mg = getMg();
        boolean validLocationsFlag = false;

        if (start instanceof Storage) {
            for (Location vAdj : mg.adjVertices(start))
                if (vAdj instanceof Country) {
                    validLocationsFlag = true;
                    break;
                }

            if (!validLocationsFlag)
                return null;

            validLocationsFlag = false;
        }

        if (end instanceof Storage) {
            for (Location vAdj : mg.adjVertices(end))
                if (vAdj instanceof Country) {
                    validLocationsFlag = true;
                    break;
                }

            if (!validLocationsFlag)
                return null;
        }

        return Algorithms.dijkstraLandMaritimePath(getMg(), start, end, false);
    }

    public LinkedList<Location> shortestMaritimePath(Storage start, Storage end) {
        return Algorithms.dijkstraLandMaritimePath(getMg(), start, end, false);
    }

    /**
     * Calculates the total distance for any given path as long as it has a corresponding edge
     * @param path the path to be calculated
     * @return the total distance of the path
     */
    public double getPathDistance(LinkedList<Location> path){
        double distance = 0;
        for (int i = 1; i < path.size(); i++){
            distance += mg.edge(path.get(i - 1), path.get(i)).getWeight();
        }
        return distance;
    }

    /**
     * returns the graph
     * @return returns a clone of graph
     */
    public MatrixGraph<Location, Double> getMg() {
        return mg.clone();
    }
}
