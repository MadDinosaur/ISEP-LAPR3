package lapr.project.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    private String mmsi = "111111111";
    private String shipName = "ship name";
    private int imo = 1000000;
    private String callSign = "CS111";
    private int vesselType = 0;
    private float length = 0;
    private float width = 0;
    private float draft = 0;

    @Test
    public void createValidShipTest(){
        Ship ship = new Ship(mmsi, shipName, imo, callSign, vesselType, length, width, draft);
        assertNotNull(ship);
        assertEquals(ship.getMmsi(), mmsi);
        assertEquals(ship.getShipName(), shipName);
        assertEquals(ship.getImo(), imo);
        ship.setImo(9999999);
        assertEquals(ship.getImo(), 9999999);
        assertEquals(ship.getCallSign(), callSign);
        assertEquals(ship.getVesselType(), vesselType);
        assertEquals(ship.getLength(), length);
        assertEquals(ship.getWidth(), width);
        assertEquals(ship.getDraft(), draft);
        assertEquals(ship.getCapacity(), 0);
        assertNull(ship.getGenerator());
    }

    @Test
    public void createIllegalDataTest(){
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            Ship ship = new Ship("1", shipName, imo, callSign, vesselType, length, width, draft);
        });

        String expectedMessage1 = "MMSI code \"1\" is not supported.";
        String actualMessage1 = exception1.getMessage();

        assertTrue(actualMessage1.contains(expectedMessage1));

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            Ship ship = new Ship(mmsi, null, imo, callSign, vesselType, length, width, draft);
        });


        String expectedMessage2 = "Ship Name Must not me null.";
        String actualMessage2 = exception2.getMessage();


        assertTrue(actualMessage2.contains(expectedMessage2));

        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
            Ship ship = new Ship(mmsi, shipName, 10000000, callSign, vesselType, length, width, draft);
        });

        String expectedMessage3 = "IMO code \"10000000\" is not supported.";
        String actualMessage3 = exception3.getMessage();

        assertTrue(actualMessage3.contains(expectedMessage3));

        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> {
            Ship ship = new Ship(mmsi, shipName, 1, callSign, vesselType, length, width, draft);
        });

        String expectedMessage4 = "IMO code \"1\" is not supported.";
        String actualMessage4 = exception4.getMessage();

        assertTrue(actualMessage4.contains(expectedMessage4));

        Exception exception5 = assertThrows(IllegalArgumentException.class, () -> {
            Ship ship = new Ship(mmsi, shipName, imo, null, vesselType, length, width, draft);
        });

        String expectedMessage5 = "Call Name Must not me null.";
        String actualMessage5 = exception5.getMessage();

        assertTrue(actualMessage5.contains(expectedMessage5));

        Exception exception6 = assertThrows(IllegalArgumentException.class, () -> {
            Ship ship = new Ship(mmsi, shipName, imo, callSign, -1, length, width, draft);
        });

        String expectedMessage6 = "The vessel type code \"-1\" is not supported.";
        String actualMessage6 = exception6.getMessage();

        assertTrue(actualMessage6.contains(expectedMessage6));

        Exception exception7 = assertThrows(IllegalArgumentException.class, () -> {
            Ship ship = new Ship(mmsi, shipName, imo, callSign, vesselType, -1, width, draft);
        });

        String expectedMessage7 = "Length  \"-1.0\" is not supported.";
        String actualMessage7= exception7.getMessage();
        assertTrue(actualMessage7.contains(expectedMessage7));

        Exception exception8 = assertThrows(IllegalArgumentException.class, () -> {
            Ship ship = new Ship(mmsi, shipName, imo, callSign, vesselType, length, -1, draft);
        });

        String expectedMessage8 = "Width  \"-1.0\" is not supported.";
        String actualMessage8 = exception8.getMessage();

        assertTrue(actualMessage8.contains(expectedMessage8));

        Exception exception9 = assertThrows(IllegalArgumentException.class, () -> {
            Ship ship = new Ship(mmsi, shipName, imo, callSign, vesselType, length, width, -1);
        });

        String expectedMessage9 = "Draft  \"-1.0\" is not supported.";
        String actualMessage9 = exception9.getMessage();

        assertTrue(actualMessage9.contains(expectedMessage9));
    }
}