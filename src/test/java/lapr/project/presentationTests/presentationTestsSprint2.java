package lapr.project.presentationTests;

import lapr.project.controller.GetManifestInformationController;
import lapr.project.controller.ReadShipFileController;
import lapr.project.controller.ReadStorageFileController;
import lapr.project.data.MainStorage;
import lapr.project.data.StorageSqlStore;
import oracle.ucp.util.Pair;
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

    @Test
    public void US207(){
        if (dataBase){
            GetManifestInformationController controller = new GetManifestInformationController();
            Pair<Integer, Integer> values = controller.findCargoManifests(1, 2020);
            StringBuilder sb = new StringBuilder();

            sb.append("YEAR - 2020 \n\n");
            sb.append("Number of manifests transported : ").append(values.get1st()).append("\n");
            sb.append("Average number of containers transported : ").append(values.get2nd()).append("\n");

            values = controller.findCargoManifests(1, 2021);

            sb.append("\n\nYEAR - 2021 \n\n");
            sb.append("Number of manifests transported : ").append(values.get1st()).append("\n");
            sb.append("Average number of containers transported : ").append(values.get2nd()).append("\n");
        }
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
