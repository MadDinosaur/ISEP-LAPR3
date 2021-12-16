package lapr.project.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ReadSeaDistFilesControllerTest {

    @Test
    public void readSeaDistFileController(){
        ReadSeaDistFilesController controller = mock(ReadSeaDistFilesController.class);
        controller.readSeaDistFileAndSaveData("seadists.csv");
        controller.readCountryFileAndSaveData("countries.csv");
        controller.readBorderFileAndSaveData("Borders.csv");
        controller.readSeaDistFileAndSaveData("");
        controller.readCountryFileAndSaveData("cuntries.csv");
        controller.readBorderFileAndSaveData("Boders.csv");
    }

}