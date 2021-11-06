package lapr.project.store;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipStoreTest {

    @Test
    public void validateTest(){
        ShipStore shipStore = new ShipStore();
        assertFalse(shipStore.addShip(null));
        assertNull(shipStore.getShipByIMO("a"));
        assertNull(shipStore.getShipByCallSign("a"));
    }
}