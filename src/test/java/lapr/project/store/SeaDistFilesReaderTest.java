package lapr.project.store;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeaDistFilesReaderTest {


    @Test
    public void readFiles(){
        assertNotNull(SeaDistFilesReader.readBorders("borders.csv"));
        assertNotNull(SeaDistFilesReader.readCountries("countries.csv"));
        assertNotNull(SeaDistFilesReader.readSeaDist("seadists.csv"));
    }
}