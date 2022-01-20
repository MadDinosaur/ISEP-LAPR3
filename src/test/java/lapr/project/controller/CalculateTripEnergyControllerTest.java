package lapr.project.controller;

import lapr.project.store.ContainerMaterialStore;
import oracle.ucp.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculateTripEnergyControllerTest {

    @Test
    public void testEnergy(){
        CalculateContainerResistivityController controller = new CalculateContainerResistivityController();

        String external = "Zinc";
        String median = "Steel";
        String internal = "Fiber-glass";

        double nonRefRes = controller.getResistivity(external, median, internal, 0.03, 0.10, 0.02, 105);

        external = "Stone Wool";
        median = "Steel";
        internal = "Iron";

        double refRes = controller.getResistivity(external, median, internal, 0.07, 0.10, 0.06, 105);

        List<Pair<Integer, Integer>> baseTemp = new ArrayList<>();
        baseTemp.add(new Pair<>(30, 23));
        baseTemp.add(new Pair<>(20, 17));
        baseTemp.add(new Pair<>(50, 29));

        CalculateTripEnergyController controller1 = new CalculateTripEnergyController();
        assertEquals(1017330274, controller1.getEnergy(100, 0, baseTemp, refRes, nonRefRes));
        assertEquals( 2.564503892 * Math.pow(10, 9), controller1.getEnergy(0, 100, baseTemp, refRes, nonRefRes));

    }
}