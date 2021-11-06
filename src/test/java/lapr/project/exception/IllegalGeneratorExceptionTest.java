package lapr.project.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IllegalGeneratorExceptionTest {

    @Test
    public void IllegalGeneratorTest(){
        Exception exception1 = assertThrows(IllegalGeneratorException.class, () -> {
            throw new IllegalGeneratorException();
        });
        Exception exception2 = assertThrows(IllegalGeneratorException.class, () -> {
            throw new IllegalGeneratorException("exception");
        });
        assertNull(exception1.getMessage());
        assertEquals(exception2.getMessage(), "exception");
    }
}