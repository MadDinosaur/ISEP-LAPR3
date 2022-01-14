package lapr.project.model.graph;

import lapr.project.model.Location;
import lapr.project.model.graph.matrix.MatrixGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmsTest {

    final Graph<String,Integer> completeMap = new MatrixGraph<>(false);
    Graph<String,Integer> incompleteMap = new MatrixGraph<>(false);

    @BeforeEach
    public void setUp() {

        completeMap.addVertex("Porto");
        completeMap.addVertex("Braga");
        completeMap.addVertex("Vila Real");
        completeMap.addVertex("Aveiro");
        completeMap.addVertex("Coimbra");
        completeMap.addVertex("Leiria");

        completeMap.addVertex("Viseu");
        completeMap.addVertex("Guarda");
        completeMap.addVertex("Castelo Branco");
        completeMap.addVertex("Lisboa");
        completeMap.addVertex("Faro");

        completeMap.addEdge("Porto", "Aveiro", 75);
        completeMap.addEdge("Porto", "Braga", 60);
        completeMap.addEdge("Porto", "Vila Real", 100);
        completeMap.addEdge("Viseu", "Guarda", 75);
        completeMap.addEdge("Guarda", "Castelo Branco", 100);
        completeMap.addEdge("Aveiro", "Coimbra", 60);
        completeMap.addEdge("Coimbra", "Lisboa", 200);
        completeMap.addEdge("Coimbra", "Leiria", 80);
        completeMap.addEdge("Aveiro", "Leiria", 120);
        completeMap.addEdge("Leiria", "Lisboa", 150);

        incompleteMap = completeMap.clone();

        completeMap.addEdge("Aveiro", "Viseu", 85);
        completeMap.addEdge("Leiria", "Castelo Branco", 170);
        completeMap.addEdge("Lisboa", "Faro", 280);
    }

    @Test
    void floydWarshallAlgorithm() {
        int [] ExpectedDistance = {
                60, 100, 75, 135, 195, 160, 235, 335, 335, 615,
                60, 160, 135, 195, 255, 220, 295, 395, 395, 675,
                100, 160, 175, 235, 295, 260, 335, 435, 435, 715,
                75, 135, 175, 60, 120, 85, 160, 260, 260, 540,
                135, 195, 235, 60, 80, 145, 220, 250, 200, 480,
                195, 255, 295, 120, 80, 205, 270, 170, 150, 430,
                160, 220, 260, 85, 145, 205, 75, 175, 345, 625,
                235, 295, 335, 160, 220, 270, 75, 100, 420, 700,
                335, 395, 435, 260, 250, 170, 175, 100, 320, 600,
                335, 395, 435, 260, 200, 150, 345, 420, 320, 280,
                615, 675, 715, 540, 480, 430, 625, 700, 600, 280};

        Graph<String, Integer> minDist = Algorithms.FloydWarshallAlgorithm(completeMap, Integer::compare, Integer::sum);

        int k = 0;
        for (int i = 0; i < completeMap.numVertices(); i++){
            for (int j = 0; j < completeMap.numVertices(); j++){
                if (j != i)
                    assertEquals(ExpectedDistance[k++], minDist.edge(i,j).getWeight());
            }
        }
    }

    @Test
    void cyclesAlgorythm() {
        ArrayList<LinkedList<String>> list = Algorithms.vertCycles(completeMap, "Lisboa", Integer::compareTo);

        assertEquals(list.size(), 6);

        for (LinkedList<String> path: list){
            assertTrue(path.contains("Lisboa"));
        }
    }
}