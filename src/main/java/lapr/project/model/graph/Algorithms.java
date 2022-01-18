package lapr.project.model.graph;


import lapr.project.model.graph.matrix.MatrixGraph;

import java.util.*;
import java.util.function.BinaryOperator;


public class Algorithms {

    /** Calculates the minimum distance graph using Floyd-Warshall
     *
     * @param g initial graph
     * @param ce comparator between elements of type E
     * @param sum sum two elements of type E
     * @return the minimum distance graph
     */
    public static <V,E> MatrixGraph<V,E> FloydWarshallAlgorithm(Graph<V,E> g, Comparator<E> ce, BinaryOperator<E> sum){

        int numV = g.numVertices();

        E[][] matrixEdges = (E[][]) new Object[numV][numV];
        int i, j, k;

        for (i = 0; i < numV; i++)
            for (j = 0; j < numV; j++) {
                Edge<V, E> e = g.edge(i, j);
                if (e != null)
                    matrixEdges[i][j] = e.getWeight();
            }

        // Adding vertices individually
        for (k = 0; k < numV; k++) {
            for (i = 0; i < numV; i++) {
                if (i != k && matrixEdges[i][k] != null)
                    for (j = 0; j < numV; j++) {
                        if (matrixEdges[k][j] != null)
                            if (matrixEdges[i][j] == null || ce.compare(matrixEdges[i][j], sum.apply(matrixEdges[i][k], matrixEdges[k][j])) > 0)
                                matrixEdges[i][j] = sum.apply(matrixEdges[i][k], matrixEdges[k][j]);
                    }
            }
        }
        return new MatrixGraph<V,E>(false,g.vertices(),matrixEdges);
    }


    /**
     * This Method runs a modified version of the allPaths algorithm where now it doesn't backtrack
     * This way it makes all the cycles a simple depthFirstSearch can find in a first reading and adds them to a single list
     * after which the method only returns the cycles that contain in them the provided vertex as these can be moved in a way where the vertex is at the start
     * @param g initial graph
     * @param vOrig the desired vertex
     * @return returns an arraylist listing all the cycles where the vertex is present
     */
    public static <V, E> ArrayList<LinkedList<V>> vertCycles(Graph<V, E> g, V vOrig, Comparator<E> ce) {
        boolean[] visited = new boolean[g.numVertices()];
        ArrayList<LinkedList<V>> paths =  new ArrayList<>();
        for (V v: g.vertices()) {
            Arrays.fill(visited, false);
            allPathNoBacktracking(g, v, v, visited, new LinkedList<>(), paths, ce);
        }
        paths.removeIf(path -> !path.contains(vOrig));
        return paths;
    }

    /**
     * The initial allPath's algorithm has a complexity of O(V!) which is unusable for graphs as big as the one we are using
     * Therefore this method does not backtrack but is made up for the fact that it is run for every
     * vertex in the graph in order to obtain a better certainty.
     * It is an heuristic that does not give 100% certain results but is pretty close and the complexity is a nice O(V)
     * @param g initial graph
     * @param vOrig the vertex which will loop on itself
     * @param visited an array with l the visited node for the current search
     * @param path a list with the current path being searched
     * @param paths a list with all the cycles that have been found
     */
    private static <V, E> void allPathNoBacktracking(Graph<V, E> g, V vOrig, V vDest, boolean[] visited,
                                                     LinkedList<V> path, ArrayList<LinkedList<V>> paths, Comparator<E> ce) {
        path.push(vOrig);
        visited[g.key(vOrig)]=true;
        ArrayList<V> adjVertices = (ArrayList<V>) g.adjVertices(vOrig);
        adjVertices.sort(new Comparator<V>() {
            @Override
            public int compare(V v, V t1) {
                return ce.compare(g.edge(vOrig, v).getWeight(), g.edge(vOrig, t1).getWeight());
            }
        });

        for (V vAdj : adjVertices){
            if (vAdj == vDest) {
                path.push(vDest);
                LinkedList<V> reversed = new LinkedList<V>();
                for (V v : path) {
                    reversed.push(v);
                }
                paths.add(reversed);
                path.pop();
            }
            else
            if (!visited[g.key(vAdj)])
                allPathNoBacktracking(g,vAdj,vDest,visited,path,paths, ce);
        }
        path.pop();
    }

    /**
     * Gets a list of lists with all the possible permutations of a given list of elements
     * @param original the list of elements
     * @return a list of list with all the possible permutations
     */
    public static <E> List<List<E>> generatePerm(List<E> original) {

        if (original.isEmpty()) {
            List<List<E>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        }
        E firstElement = original.remove(0);
        List<List<E>> returnValue = new ArrayList<>();
        List<List<E>> permutations = generatePerm(original);

        for (List<E> smallerPermutated : permutations) {
            for (int index = 0; index <= smallerPermutated.size(); index++) {
                List<E> temp = new ArrayList<>(smallerPermutated);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }

        return returnValue;
    }

    /**
     * Computes shortest-path distance from a source vertex to all reachable
     * vertices of a graph g with non-negative edge weights
     * This implementation uses Dijkstra's algorithm
     *
     * @param g        Graph instance
     * @param vOrig    Vertex that will be the source of the path
     * @param visited  set of previously visited vertices
     * @param pathKeys minimum path vertices keys
     * @param dist     minimum distances
     */
    private static <V, E> void shortestPathDijkstra(Graph<V, E> g, V vOrig,
                                                    Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                                    boolean[] visited, V [] pathKeys, E [] dist) {
        for (int i = 0; i < g.numVertices(); i++){
            dist[i] = null;
            pathKeys[i] = null;
            visited[i] = false;
        }

        dist[g.key(vOrig)] = zero;

        while (vOrig != null) {

            int vOrigKey = g.key(vOrig);
            visited[vOrigKey] = true;

            for (V vAdj : g.adjVertices(vOrig)) {
                Edge<V,E> e = g.edge(vOrig, vAdj);
                int vAdjKey = g.key(vAdj);
                if (!visited[vAdjKey] && (dist[vAdjKey] == null || ce.compare(dist[vAdjKey], sum.apply(dist[vOrigKey], e.getWeight())) > 0)) {
                    dist[vAdjKey] = sum.apply(dist[vOrigKey], e.getWeight());
                    pathKeys[vAdjKey] = vOrig;
                }
            }

            vOrig = null;

            E minDistance = null;

            for (V vert : g.vertices()) {
                int vVertKey = g.key(vert);
                if (!visited[vVertKey] && dist[vVertKey] != null && (minDistance == null || ce.compare(dist[vVertKey], minDistance) < 0)) {
                    vOrig = vert;
                    minDistance = dist[vVertKey];
                }
            }
        }
    }

    /** Shortest-path between two vertices
     *
     * @param g graph
     * @param vOrig origin vertex
     * @param vDest destination vertex
     * @param ce comparator between elements of type E
     * @param sum sum two elements of type E
     * @param zero neutral element of the sum in elements of type E
     * @param shortPath returns the vertices which make the shortest path
     * @return if vertices exist in the graph and are connected, true, false otherwise
     */
    public static <V, E> E shortestPath(Graph<V, E> g, V vOrig, V vDest,
                                        Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                        LinkedList<V> shortPath) {
        if (!g.validVertex(vOrig) || !g.validVertex(vDest))
            return null;

        shortPath.clear();

        int nVerts = g.numVertices();

        boolean[] visited = new boolean[nVerts]; //default value: false
        V[] pathKeys = (V[]) new Object[nVerts];
        E[] dist = (E[]) new Object[nVerts];

        shortestPathDijkstra(g, vOrig,  ce, sum, zero, visited, pathKeys, dist);

        if (dist[g.key(vDest)] != null) {
            getPath(g, vOrig, vDest, pathKeys, shortPath);
        }

        return shortPath.isEmpty() ? null : dist[g.key(vDest)];
    }

    /** Shortest-path between a vertex and all other vertices
     *
     * @param g graph
     * @param vOrig start vertex
     * @param ce comparator between elements of type E
     * @param sum sum two elements of type E
     * @param zero neutral element of the sum in elements of type E
     * @param paths returns all the minimum paths
     * @param dists returns the corresponding minimum distances
     * @return if vOrig exists in the graph true, false otherwise
     */
    public static <V, E> boolean shortestPaths(Graph<V, E> g, V vOrig,
                                               Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                               ArrayList<LinkedList<V>> paths, ArrayList<E> dists) {

        if (!g.validVertex(vOrig))
            return false;

        int nVerts = g.numVertices();

        boolean[] visited = new boolean[nVerts]; //default value: false
        V[] pathKeys = (V[]) new Object[nVerts];
        E[] dist = (E[]) new Object[nVerts];

        shortestPathDijkstra(g, vOrig,  ce, sum, zero, visited, pathKeys, dist);

        dists.clear();
        paths.clear();
        for (int i = 0; i < nVerts; i++) {
            paths.add(null);
            dists.add(null);
        }

        for (int i = 0; i < nVerts; i++) {
            LinkedList<V> shortPath = new LinkedList<>();
            if (dist[i] != null)
                getPath(g, vOrig, g.vertex(i), pathKeys, shortPath);
            paths.set(i, shortPath);
            dists.set(i, dist[i]);
        }
        return true;
    }

    /**
     * Extracts from pathKeys the minimum path between voInf and vdInf
     * The path is constructed from the end to the beginning
     *
     * @param g        Graph instance
     * @param vOrig    information of the Vertex origin
     * @param vDest    information of the Vertex destination
     * @param pathKeys minimum path vertices keys
     * @param path     stack with the minimum path (correct order)
     */
    private static <V, E> void getPath(Graph<V, E> g, V vOrig, V vDest,
                                       V [] pathKeys, LinkedList<V> path) {

        path.addFirst(vDest);
        int vKey = g.key(pathKeys[g.key(vDest)]);
        if (vKey != -1) {
            vDest = g.vertex(vKey);
            getPath(g, vOrig, vDest, pathKeys, path);
        }
    }
}
