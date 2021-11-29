package lapr.project.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IllegalStorageExceptionTest {
    @Test
    public void IllegalStorageExceptionTest(){
        Exception exception1 = assertThrows(IllegalStorageException.class, () -> {
            throw new IllegalStorageException();
        });

        Exception exception2 = assertThrows(IllegalStorageException.class, () -> {
            throw new IllegalStorageException("exception");
        });

        assertNull(exception1.getMessage());
        assertEquals(exception2.getMessage(), "exception");
    }

}