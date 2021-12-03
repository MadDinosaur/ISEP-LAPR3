package lapr.project.presentationTests;

import lapr.project.controller.ReadShipFileController;
import lapr.project.controller.ReadStorageFileController;
import lapr.project.data.MainStorage;
import lapr.project.data.StorageSqlStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;

public class presentationTestsSprint2 {

    boolean dataBase = false;

//    @Test
//    public void insert(){
//            ReadStorageFileController readStorageFileController = new ReadStorageFileController();
//            readStorageFileController.readFileAndSaveData("bports.csv");
//            ReadShipFileController readShipFileController = new ReadShipFileController();
//            readShipFileController.readFileAndSaveData("sships.csv");
//    }

    @Test
    public void US201(){
        if (!dataBase) {
            ReadStorageFileController readStorageFileController = new ReadStorageFileController();
            readStorageFileController.readFileAndSaveData("bports.csv");

        } else {
            StorageSqlStore storageSqlStore = new StorageSqlStore();
            MainStorage.getInstance().getStorageStore().addStorageList(storageSqlStore.getStorageDataFromDataBase(MainStorage.getInstance().getDatabaseConnection()));
        }

        StringBuilder sb = new StringBuilder();

        sb.append("RESULTING TREE SIZE : ");
        sb.append(MainStorage.getInstance().getStorageStore().size());
        sb.append("\n\n\nRESULTING TREE\n\n\n");
        sb.append(MainStorage.getInstance().getStorageStore().toString());
        sb.append("\n\n\nRESULTING TREE'S BALANCE NODE\n\n\n");
        sb.append(MainStorage.getInstance().getStorageStore().balanceFactor());
        writeOutput(sb.toString(), "US201");
    }

    private void writeOutput(String output, String filename){
        try (FileWriter myWriter = new FileWriter("output\\sprint2\\" + filename + ".txt")) {
            if(output!=null)
                myWriter.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
