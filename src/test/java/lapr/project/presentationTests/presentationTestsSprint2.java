package lapr.project.presentationTests;

import lapr.project.controller.ReadStorageFileController;
import org.junit.jupiter.api.BeforeEach;

public class presentationTestsSprint2 {


    @BeforeEach
    private void setUp(){
        ReadStorageFileController readStorageFileController = new ReadStorageFileController();
        readStorageFileController.readFileAndSaveData("bports.csv");
    }
}
