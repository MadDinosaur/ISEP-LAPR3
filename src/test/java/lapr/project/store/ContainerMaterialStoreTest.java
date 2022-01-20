package lapr.project.store;

import lapr.project.controller.CalculateContainerResistivityController;
import lapr.project.model.Container;
import oracle.ucp.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContainerMaterialStoreTest {

    @Test
    public void materialTest(){
        ContainerMaterialStore containerMaterialStore = new ContainerMaterialStore();
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            containerMaterialStore.getResistivityByArea("cork", "Stone Wool", "Fiber-glass", 0.3, 0.4, 0.3);
        });

        String expectedMessage1 = "there's no such material: cork";
        String actualMessage1 = exception1.getMessage();

        assertTrue(actualMessage1.contains(expectedMessage1));

        exception1 = assertThrows(IllegalArgumentException.class, () -> {
            containerMaterialStore.getResistivityByArea("Cork", "stone Wool", "Fiber-glass", 0.3, 0.4, 0.3);
        });

        expectedMessage1 = "there's no such material: stone Wool";
        actualMessage1 = exception1.getMessage();

        assertTrue(actualMessage1.contains(expectedMessage1));

        exception1 = assertThrows(IllegalArgumentException.class, () -> {
            containerMaterialStore.getResistivityByArea("Cork", "Stone Wool", "fiber-glass", 0.3, 0.4, 0.3);
        });

        expectedMessage1 = "there's no such material: fiber-glass";
        actualMessage1 = exception1.getMessage();

        assertTrue(actualMessage1.contains(expectedMessage1));


        assertEquals(0.6544798172740687, containerMaterialStore.getResistivityByArea("Fiber-glass", "Steel", "Aluminium", 0.03, 0.1, 0.02));
        assertEquals(0.6544798172740687, containerMaterialStore.getResistivity("Fiber-glass", "Steel", "Aluminium", 0.03, 0.1, 0.02, 1));
    }

    @Test
    public void testEnergy(){
        ContainerMaterialStore controller = new ContainerMaterialStore();

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

        assertEquals(1017330274, controller.tripEnergy(100, 0, baseTemp, refRes, nonRefRes));
        assertEquals( 2.564503892 * Math.pow(10, 9), controller.tripEnergy(0, 100, baseTemp, refRes, nonRefRes));

    }
}