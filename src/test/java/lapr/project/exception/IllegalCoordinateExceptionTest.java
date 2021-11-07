package lapr.project.exception;

import lapr.project.model.Coordinate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IllegalCoordinateExceptionTest {

    @Test
    public void IllegalCoordinateTest(){
        Exception exception1 = assertThrows(IllegalCoordinateException.class, () -> {
            throw new IllegalCoordinateException();
        });
        Exception exception2 = assertThrows(IllegalCoordinateException.class, () -> {
            throw new IllegalCoordinateException("exception");
        });
        assertNull(exception1.getMessage());
        assertEquals(exception2.getMessage(), "exception");
    }
}