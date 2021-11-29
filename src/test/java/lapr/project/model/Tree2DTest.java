package lapr.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Tree2DTest {
    int[] arr = {20,15,10,13,8,17,40,50,30,7};
    int[] arrX = {10,30,10,40,60,17,35,70,25,7};
    int[] arrY = {35,15,40,50,43,10,70,25,10,15};
    Integer[] arrInOrder = {17,30,7,20,10,15,8,50,13,40};
    List<Tree2D.Node<Integer>> nodeList = new ArrayList<>();
    Tree2D<Integer> instance;

    @BeforeEach
    void setUp() {
        instance = new Tree2D<>();

        for (int i = 0; i < arr.length; i++)
            nodeList.add(new Tree2D.Node<>(arr[i], arrX[i], arrY[i]));

        instance.insert(nodeList);
    }

    @Test
    void insert() {
        int[] arr = {20,15,10,13,8,17,40,50,30,7};
        int[] arrX = {10,30,10,40,60,17,35,70,25,7};
        int[] arrY = {35,15,40,50,43,10,70,25,10,15};
        Tree2D<Integer> instance = new Tree2D<>();
        nodeList = new ArrayList<>();

        for (int i = 0; i < arr.length; i++)
            nodeList.add(new Tree2D.Node<>(arr[i], arrX[i], arrY[i]));

        instance.insert(nodeList);
        assertEquals(arr.length, instance.size());
    }

    @Test
    void findNearestNeighbour() {
        assertEquals(20, instance.findNearestNeighbour(9, 34));
        instance = new Tree2D<>();
        assertNull(instance.findNearestNeighbour(9, 34));
    }

    @Test
    void testToString() {
        String expected = "|\t|-------40\n" +
                "|-------13\n" +
                "|\t|-------50\n" +
                "|\t|\t|-------8\n" +
                "15\n" +
                "|\t|-------10\n" +
                "|\t|\t|-------20\n" +
                "|-------7\n" +
                "|\t|-------30\n" +
                "|\t|\t|-------17\n";

        String result = instance.toString();

        assertEquals(expected, result);

        instance = new Tree2D<>();

        assertTrue(instance.toString().isEmpty());
    }

    @Test
    void inOrder() {
        List<Integer> lExpected = Arrays.asList(arrInOrder);

        assertEquals(lExpected, instance.inOrder());
    }
}