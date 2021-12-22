package lapr.project.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculateContainerResistivityControllerTest {

    @Test
    public void containerTest(){
        CalculateContainerResistivityController containerResistivityController = new CalculateContainerResistivityController();
        assertEquals(0.6544798172740687, containerResistivityController.getResistivityByArea("Fiber-glass", "Steel", "Aluminium", 0.03, 0.1, 0.02));
        assertEquals(0.6544798172740687, containerResistivityController.getResistivity("Fiber-glass", "Steel", "Aluminium", 0.03, 0.1, 0.02, 1));

    }

}