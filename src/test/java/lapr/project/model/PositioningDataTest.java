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
    private float invalidCog = -1;
    private float invalidSog = -1;

    @Test
    public void createValidPositionDataTest(){
        PositioningData positioningData = new PositioningData(bdt, coordinate, sog, cog, heading, position, transceiverClass);
        assertNotNull(positioningData);
        assertEquals(positioningData.getCoordinate(), coordinate);
        assertEquals(positioningData.getSog(), sog);
        positioningData.setSog(0);
        assertEquals(positioningData.getSog(), 0);
        positioningData.setSog(-10);
        assertEquals(positioningData.getSog(), 0);
        assertEquals(positioningData.getCog(), cog);
        positioningData.setCog(-359);
        assertEquals(positioningData.getCog(), 0);
        positioningData.setCog(0);
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
    public void createInvalidDataTest(){
        PositioningData positioningData = new PositioningData(bdt, coordinate, invalidSog, invalidCog, heading, position, transceiverClass);
        positioningData.setCog(invalidCog);
        assertEquals(358,positioningData.getCog());
        positioningData.setSog(invalidSog);
        assertEquals(0,positioningData.getSog());
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

        Exception exception5 = assertThrows(IllegalArgumentException.class, () -> {
            PositioningData positioningData = new PositioningData(bdt, coordinate, sog, -370, heading, position, transceiverClass);
        });


        String expectedMessage5 = "COG value \"-370.0\"  is not withing boundaries";
        String actualMessage5 = exception5.getMessage();

        assertTrue(actualMessage5.contains(expectedMessage5));


        Exception exception7 = assertThrows(IllegalArgumentException.class, () -> {
            PositioningData positioningData = new PositioningData(bdt, coordinate, sog, cog, -400, position, transceiverClass);
        });

        String expectedMessage7  = "Heading value \"-400.0\"  is not withing boundaries";
        String actualMessage7  = exception7 .getMessage();

        assertTrue(actualMessage7.contains(expectedMessage7));
    }
}