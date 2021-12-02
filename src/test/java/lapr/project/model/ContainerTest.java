package lapr.project.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContainerTest {
    int containerNum = 200031;
    int checkDigit = 7;
    String isoCode = "22G1";
    double grossWeight =  30480.0;
    double tareWeight = 2180.0;
    double payload = 28300.0;
    double maxVol = 33.1;
    boolean refrigerated = false;

    @Test
    void CreateContainerTest() {
        Container c1 = new Container(containerNum,checkDigit,isoCode,grossWeight,tareWeight,payload,maxVol,refrigerated);
        assertNotNull(c1);
        assertEquals(containerNum, c1.getContainerNum());
        assertEquals(checkDigit, c1.getCheckDigit());
        assertEquals(isoCode, c1.getIsoCode());
        assertEquals(grossWeight, c1.getGrossWeight());
        assertEquals(tareWeight, c1.getTareWeight());
        assertEquals(payload, c1.getPayload());
        assertFalse(c1.isRefrigerated());
    }

    @Test
    void checkParametersContainerTest() {
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            Container container = new Container(containerNum, 10000, isoCode, grossWeight, tareWeight, payload, maxVol, refrigerated);
        });

        String expectedMessage1 = "Check digit must be between 0 and 9.";
        String actualMessage1 = exception1.getMessage();

        assertTrue(actualMessage1.contains(expectedMessage1));

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            Container container = new Container(containerNum, checkDigit, "AAAAAAA", grossWeight, tareWeight, payload, maxVol, refrigerated);
        });

        String expectedMessage2 = "Iso code must have 4 characters.";
        String actualMessage2 = exception2.getMessage();

        assertTrue(actualMessage2.contains(expectedMessage2));

        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
            Container container = new Container(containerNum, checkDigit, isoCode, -1, tareWeight, payload, maxVol, refrigerated);
        });

        String expectedMessage3 = "Gross weight must be a positive number.";
        String actualMessage3 = exception3.getMessage();

        assertTrue(actualMessage3.contains(expectedMessage3));

        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> {
            Container container = new Container(containerNum, checkDigit, isoCode, grossWeight, -1, payload, maxVol, refrigerated);
        });

        String expectedMessage4 = "Tare weight must be a positive number.";
        String actualMessage4 = exception4.getMessage();

        assertTrue(actualMessage4.contains(expectedMessage4));

        Exception exception5 = assertThrows(IllegalArgumentException.class, () -> {
            Container container = new Container(containerNum, checkDigit, isoCode, grossWeight, tareWeight, -1, maxVol, refrigerated);
        });

        String expectedMessage5 = "Payload must be a positive number.";
        String actualMessage5 = exception5.getMessage();

        assertTrue(actualMessage5.contains(expectedMessage5));

        Exception exception6 = assertThrows(IllegalArgumentException.class, () -> {
            Container container = new Container(containerNum, checkDigit, isoCode, grossWeight, tareWeight, payload, -1, refrigerated);
        });

        String expectedMessage6 = "Max. volume must be a positive number.";
        String actualMessage6 = exception6.getMessage();

        assertTrue(actualMessage6.contains(expectedMessage6));
    }
}