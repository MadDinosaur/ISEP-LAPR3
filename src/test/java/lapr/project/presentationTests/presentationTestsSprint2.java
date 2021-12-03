package lapr.project.presentationTests;

import lapr.project.controller.ReadStorageFileController;
import lapr.project.data.MainStorage;
import lapr.project.data.StorageSqlStore;
import org.junit.jupiter.api.BeforeEach;

public class presentationTestsSprint2 {

    boolean dataBase = false;

    @BeforeEach
    private void setUp(){
        ReadStorageFileController readStorageFileController = new ReadStorageFileController();
        if (!dataBase) {
            readStorageFileController.readFileAndSaveData("bports.csv");
        } else {
            StorageSqlStore storageSqlStore = new StorageSqlStore();
            MainStorage.getInstance().getStorageStore().addStorageList(storageSqlStore.getStorageDataFromDataBase(MainStorage.getInstance().getDatabaseConnection()));
        }
    }
}
