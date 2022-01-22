package lapr.project.controller;

import oracle.ucp.util.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CenterOfMassShipControllerTest {

    double massShip = 55000000;
    double length = 399.2;
    double width = 58.5;
    double massTower = 100000;
    Pair<Double,Double> coordinatesTower = new Pair<>(216.6, 29.3);

    @Test
    public void centerOfMass(){
        CenterOfMassShipController controller = new CenterOfMassShipController();
        Pair<Double,Double> result = controller.getCenterOfMass(massShip,length,width,massTower,coordinatesTower);
        assertEquals(199.63085299455534, result.get1st());
        assertEquals(29.250090744101634, result.get2nd());
    }
}