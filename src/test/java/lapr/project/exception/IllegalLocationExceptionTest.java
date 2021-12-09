package lapr.project.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IllegalLocationExceptionTest {

    @Test
    public void IllegalShipTest(){
        Exception exception1 = assertThrows(IllegalLocationException.class, () -> {
            throw new IllegalLocationException();
        });
        Exception exception2 = assertThrows(IllegalLocationException.class, () -> {
            throw new IllegalLocationException("exception");
        });
        assertNull(exception1.getMessage());
        assertEquals(exception2.getMessage(), "exception");
    }

}