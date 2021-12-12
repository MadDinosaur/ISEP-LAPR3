package lapr.project.model.graph;

import lapr.project.model.Storage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {


    @Test
    public void createEdgeException(){
        Exception exception1 = assertThrows(RuntimeException.class, () ->{
            Edge<Integer, Integer> edge = new Edge<>(null, 3, 2);
        });

        String expectedMessage1 = "Edge vertices cannot be null!";
        String actualMessage1 = exception1.getMessage();

        assertTrue(actualMessage1.contains(expectedMessage1));

        Exception exception2 = assertThrows(RuntimeException.class, () ->{
            Edge<Integer, Integer> edge = new Edge<>(3, null, 2);
        });

        String expectedMessage2 = "Edge vertices cannot be null!";
        String actualMessage2 = exception2.getMessage();

        assertTrue(actualMessage2.contains(expectedMessage2));

        Exception exception3 = assertThrows(RuntimeException.class, () ->{
            Edge<Integer, Integer> edge = new Edge<>(null, null, 2);
        });

        String expectedMessage3 = "Edge vertices cannot be null!";
        String actualMessage3 = exception3.getMessage();

        assertTrue(actualMessage3.contains(expectedMessage3));
    }

    @Test
    public void equalsTest(){
        Edge<Integer, Integer> edge = new Edge<>(3, 2, 2);
        assertEquals(edge, edge);
        assertNotEquals(edge, "a");
        assertNotEquals(edge, null);
        edge.setWeight(3);
        assertNotNull(edge.hashCode());
    }
}