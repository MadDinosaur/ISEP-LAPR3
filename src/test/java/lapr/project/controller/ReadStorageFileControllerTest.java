package lapr.project.controller;

import lapr.project.data.MainStorage;
import lapr.project.model.Coordinate;
import lapr.project.model.Storage;
import lapr.project.store.ShipStore;
import lapr.project.store.StorageStore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ReadStorageFileControllerTest {

    @Test
    public void controllerTest(){
        ReadStorageFileController readStorageController = new ReadStorageFileController();
        readStorageController.readFileAndSaveData("bports.csv");
        assertNotNull(MainStorage.getInstance().getStorageStore().findNearestNeighbour(-3,53));
    }

    @Test
    public void controllerNoFileTest(){
        ReadStorageFileController readStorageController = new ReadStorageFileController();
        readStorageController.readFileAndSaveData("asd");
        assertNull(MainStorage.getInstance().getStorageStore().findNearestNeighbour(-3,53));
    }

}
