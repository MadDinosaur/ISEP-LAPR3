package lapr.project.controller;

import lapr.project.model.Location;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class GetPlaceClosestToAllPlacesControllerTest {

    @Test
    public void controllerTest(){
        GetPlaceClosestToAllPlacesController controller = new GetPlaceClosestToAllPlacesController();
        assertNotNull(controller);
        RegisterNewUserController controller1 = new RegisterNewUserController();
        assertNotNull(controller1);
    }
}