package lapr.project.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IllegalShipExceptionTest {

    @Test
    public void IllegalShipTest(){
        Exception exception1 = assertThrows(IllegalShipException.class, () -> {
            throw new IllegalShipException();
        });
        Exception exception2 = assertThrows(IllegalShipException.class, () -> {
            throw new IllegalShipException("exception");
        });
        assertNull(exception1.getMessage());
        assertEquals(exception2.getMessage(), "exception");
    }
}