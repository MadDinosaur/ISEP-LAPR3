package lapr.project.model;

import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class PositioningDataTest {
    private String bdt = "31/12/2000 23:38";
    private Coordinate coordinate = new Coordinate(79, 79);
    private float sog = 0;
    private float cog = 359;
    private float heading = 0;
    private String position = "95";
    private String transceiverClass = "A";

    @Test
    public void createValidPositionDataTest(){
        PositioningData positioningData = new PositioningData(bdt, coordinate, sog, cog, heading, position, transceiverClass);
        assertNotNull(positioningData);
        assertEquals(positioningData.getBdt().toString(), "Sun Dec 31 23:38:00 WET 2000");
        assertEquals(positioningData.getCoordinate(), coordinate);
        assertEquals(positioningData.getSog(), sog);
        assertEquals(positioningData.getCog(), cog);
        positioningData.setCog(-359);
        assertEquals(positioningData.getCog(), 0);
        assertEquals(positioningData.getHeading(), heading);
        positioningData.setHeading(359);
        assertEquals(positioningData.getHeading(), 359);
        positioningData.setHeading(511);
        assertEquals(positioningData.getHeading(), 511);
        assertEquals(positioningData.getPosition(), position);
        assertEquals(positioningData.getTransceiverClass(), transceiverClass);
    }

    @Test
    public void createIllegalDataTest(){
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            PositioningData positioningData = new PositioningData("asd", coordinate, sog, cog, heading, position, transceiverClass);
        });

        String expectedMessage1 = "Base Date time value \"asd\"  is not accepted";
        String actualMessage1 = exception1.getMessage();

        assertTrue(actualMessage1.contains(expectedMessage1));

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            PositioningData positioningData = new PositioningData(bdt, coordinate, sog, 370, heading, position, transceiverClass);
        });


        String expectedMessage2 = "COG value \"370.0\"  is not withing boundaries";
        String actualMessage2 = exception2.getMessage();

        System.out.println(actualMessage2);
        assertTrue(actualMessage2.contains(expectedMessage2));

        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
            PositioningData positioningData = new PositioningData(bdt, coordinate, sog, cog, 400, position, transceiverClass);
        });

        String expectedMessage3 = "Heading value \"400.0\"  is not withing boundaries";
        String actualMessage3 = exception3.getMessage();

        assertTrue(actualMessage3.contains(expectedMessage3));

        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> {
            PositioningData positioningData = new PositioningData(bdt, coordinate, sog, cog, heading, position, null);
        });

        String expectedMessage4 = "Transceiver class must not be null";
        String actualMessage4 = exception4.getMessage();

        assertTrue(actualMessage4.contains(expectedMessage4));
    }
}