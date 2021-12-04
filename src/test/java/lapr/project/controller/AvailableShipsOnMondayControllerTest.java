package lapr.project.controller;

import oracle.ucp.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AvailableShipsOnMondayControllerTest {

    @Test
    void getAvailableShipsOnMonday() {
        AvailableShipsOnMondayController controller = mock(AvailableShipsOnMondayController.class);

        List<Pair<Integer, Integer>> resultList = new ArrayList<>();

        resultList.add(new Pair<>(2, 10));
        resultList.add(new Pair<>(5, 12));
        resultList.add(new Pair<>(1, 21));

        when(controller.getAvailableShipsOnMonday()).thenReturn(resultList);

        List<Pair<Integer, Integer>> result = controller.getAvailableShipsOnMonday();

        assertNotNull(result);
        assertEquals(result, resultList);
    }

    @Test
    void availableShipsOnMondayToString() {
        List<Pair<Integer, Integer>> resultList = new ArrayList<>();

        resultList.add(new Pair<>(2, 10));
        resultList.add(new Pair<>(5, 12));
        resultList.add(new Pair<>(1, 21));

        AvailableShipsOnMondayController controller = new AvailableShipsOnMondayController();

        String result = controller.AvailableShipsOnMondayToString(resultList);

        String expected = "The available ports and their respective location next week's monday are: \n" +
                "1. Ship's mmsi: 2 || Port's identification number (where it will be located): 10\n" +
                "2. Ship's mmsi: 5 || Port's identification number (where it will be located): 12\n" +
                "3. Ship's mmsi: 1 || Port's identification number (where it will be located): 21\n";

        assertNotNull(result);
        assertEquals(expected, result);
    }
}