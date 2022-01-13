package lapr.project.model.graph;


import lapr.project.model.graph.matrix.MatrixGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
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
    public static <V, E> ArrayList<LinkedList<V>> vertCycles(Graph<V, E> g, V vOrig) {
        boolean[] visited = new boolean[g.numVertices()];
        ArrayList<LinkedList<V>> paths =  new ArrayList<>();
        for(V v: g.vertices()) {
            Arrays.fill(visited, false);
            allPathNoBacktracking(g, v, v, visited, new LinkedList<>(), paths);
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
                                                     LinkedList<V> path, ArrayList<LinkedList<V>> paths) {
        path.push(vOrig);
        visited[g.key(vOrig)]=true;
        for (V vAdj : g.adjVertices(vOrig)){
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
                allPathNoBacktracking(g,vAdj,vDest,visited,path,paths);
        }
        path.pop();
    }
}
