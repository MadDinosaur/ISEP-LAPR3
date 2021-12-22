package lapr.project.store;

import lapr.project.model.Container;
import org.junit.jupiter.api.Test;

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

}