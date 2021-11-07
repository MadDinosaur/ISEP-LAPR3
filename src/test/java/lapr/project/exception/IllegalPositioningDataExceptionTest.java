package lapr.project.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IllegalPositioningDataExceptionTest {

    @Test
    public void IllegalPositioningTest(){
        Exception exception1 = assertThrows(IllegalPositioningDataException.class, () -> {
            throw new IllegalPositioningDataException();
        });
        Exception exception2 = assertThrows(IllegalPositioningDataException.class, () -> {
            throw new IllegalPositioningDataException("exception");
        });
        assertNull(exception1.getMessage());
        assertEquals(exception2.getMessage(), "exception");
    }
}