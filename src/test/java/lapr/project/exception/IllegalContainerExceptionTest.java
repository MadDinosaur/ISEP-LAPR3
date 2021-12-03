package lapr.project.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IllegalContainerExceptionTest {

    @Test
    public void IllegalShipTest(){
        Exception exception1 = assertThrows(IllegalContainerException.class, () -> {
            throw new IllegalContainerException();
        });
        Exception exception2 = assertThrows(IllegalContainerException.class, () -> {
            throw new IllegalContainerException("exception");
        });
        assertNull(exception1.getMessage());
        assertEquals(exception2.getMessage(), "exception");
    }
}