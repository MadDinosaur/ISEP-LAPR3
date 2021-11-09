package lapr.project.controller;

import lapr.project.data.MainStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReadShipFileControllerTest {

    @Test
    public void controllerTest(){
        ReadShipFileController readShipFileController = new ReadShipFileController();
        readShipFileController.readFileAndSaveData("sships.csv");
        assertNotNull(MainStorage.getInstance().getShipStore().getShipByMMSI("210950000"));
    }

    @Test
    public void controllerNoFileTest(){
        ReadShipFileController readShipFileController = new ReadShipFileController();
        readShipFileController.readFileAndSaveData("");
        assertNull(MainStorage.getInstance().getShipStore().getShipByMMSI("211331640"));
    }
}