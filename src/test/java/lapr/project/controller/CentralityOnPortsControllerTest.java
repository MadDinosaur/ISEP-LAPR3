package lapr.project.controller;

import lapr.project.model.Storage;
import oracle.ucp.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CentralityOnPortsControllerTest {

    @Test
    public void controllerTest(){
        CentralityOnPortsController controller = new CentralityOnPortsController();
        List<Pair<Storage, Integer>> result = controller.getCentrality(0);
        assertTrue(result.isEmpty());
    }

}