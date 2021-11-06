package lapr.project.model;

import lapr.project.exception.IllegalCoordinateException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {



    @Test
    void CreateCoordinatesTest() {
        Coordinate c1 = new Coordinate(180, 90);
        Coordinate c2 = new Coordinate(-180, 91);
        Coordinate c3 = new Coordinate(181, -90);
        assertNotNull(c1);
        assertNotNull(c2);
        assertNotNull(c3);
    }

    @Test
    public void testIllegalLongitudeValues() {
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            Coordinate a = new Coordinate((float) -180.1, 50);
        });
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            Coordinate a = new Coordinate((float) 180.1, 50);
        });

        String expectedMessage1 = "The longitude value \"-180.1\" is not within the expected boundaries";
        String actualMessage1 = exception1.getMessage();

        assertTrue(actualMessage1.contains(expectedMessage1));

        String expectedMessage2 = "The longitude value \"180.1\" is not within the expected boundaries";
        String actualMessage2 = exception2.getMessage();

        assertTrue(actualMessage2.contains(expectedMessage2));
    }

    @Test
    public void testIllegalLatitudeValues() {
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            Coordinate a = new Coordinate( 90, (float) 90.1);
        });
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            Coordinate a = new Coordinate( 90, (float) -90.1);
        });

        String expectedMessage1 = "The latitude value \"90.1\" is not within the expected boundaries";
        String actualMessage1 = exception1.getMessage();

        assertTrue(actualMessage1.contains(expectedMessage1));

        String expectedMessage2 = "The latitude value \"-90.1\" is not within the expected boundaries";
        String actualMessage2 = exception2.getMessage();

        assertTrue(actualMessage2.contains(expectedMessage2));
    }
}