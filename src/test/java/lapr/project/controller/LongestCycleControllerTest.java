package lapr.project.controller;

import lapr.project.model.Location;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class LongestCycleControllerTest {

    @Test
    public void controllerTest(){
        LongestCycleController controller = new LongestCycleController();
        LinkedList<Location> locations = controller.getLongestCycle(null);
        assertTrue(locations.isEmpty());
        double distance = controller.getCycleDistance(locations);
        assertEquals(distance, 0);
    }
}