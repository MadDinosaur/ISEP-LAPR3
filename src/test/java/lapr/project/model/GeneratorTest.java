package lapr.project.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeneratorTest {

    @Test
    void CreateGeneratorTest() {
        Generator c1 = new Generator(10, 90);
        assertEquals(c1.getGeneratorOutput(), 90);
        assertEquals(c1.getNumberOfGenerators(), 10);
    }

    @Test
    public void testIllegalGeneratorValues() {
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            Generator c1 = new Generator(0, 90);
        });
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            Generator c1 = new Generator(10, 0);
        });

        String expectedMessage1 = "Generator Number cannot be lower than 1";
        String actualMessage1 = exception1.getMessage();

        assertTrue(actualMessage1.contains(expectedMessage1));

        String expectedMessage2 = "Generator output cannot be negative";
        String actualMessage2 = exception2.getMessage();

        assertTrue(actualMessage2.contains(expectedMessage2));
    }
}