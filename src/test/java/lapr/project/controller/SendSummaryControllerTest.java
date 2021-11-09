package lapr.project.controller;

import lapr.project.data.MainStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SendSummaryControllerTest {

    //fazer before com info do abaixo
    @Test
    public void controllerTest(){
        ReadShipFileController readShipFileController = new ReadShipFileController();
        readShipFileController.readFileAndSaveData("sships.csv");
        assertNotNull(MainStorage.getInstance().getShipStore().getShipByMMSI("210950000"));
    }

    @Test
    public void getSummaryTest(){

    }
}