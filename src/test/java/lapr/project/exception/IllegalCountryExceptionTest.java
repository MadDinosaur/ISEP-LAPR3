package lapr.project.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IllegalCountryExceptionTest {

    @Test
    public void IllegalShipTest(){
        Exception exception1 = assertThrows(IllegalCountryException.class, () -> {
            throw new IllegalCountryException();
        });
        Exception exception2 = assertThrows(IllegalCountryException.class, () -> {
            throw new IllegalCountryException("exception");
        });
        assertNull(exception1.getMessage());
        assertEquals(exception2.getMessage(), "exception");
    }
}