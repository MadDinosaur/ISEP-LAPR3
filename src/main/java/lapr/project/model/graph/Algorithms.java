package lapr.project.model.graph;


import lapr.project.model.graph.CommonGraph;
import lapr.project.model.graph.Edge;
import lapr.project.model.graph.Graph;
import lapr.project.model.graph.matrix.MatrixGraph;

import java.util.Comparator;
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
}
