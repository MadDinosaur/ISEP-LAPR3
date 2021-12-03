package lapr.project.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnauthorizedOperationExceptionTest {

    @Test
    public void UnauthorizedOperationExceptionTest(){
        Exception exception1 = assertThrows(UnauthorizedOperationException.class, () -> {
            throw new UnauthorizedOperationException();
        });

        Exception exception2 = assertThrows(UnauthorizedOperationException.class, () -> {
            throw new UnauthorizedOperationException("exception");
        });

        assertNull(exception1.getMessage());
        assertEquals(exception2.getMessage(), "exception");
    }
}