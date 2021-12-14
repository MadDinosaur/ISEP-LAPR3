package lapr.project.presentationTests;

import lapr.project.controller.ContainerAuditController;
import lapr.project.controller.ReadSeaDistFilesController;
import lapr.project.controller.ReadShipFileController;
import lapr.project.controller.ReadStorageFileController;
import lapr.project.data.CountrySqlStore;
import lapr.project.data.MainStorage;
import lapr.project.model.Country;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class presentationTestsSprint3 {

    boolean dataBase = false;

//    @Test
//    public void insert(){
//        ReadSeaDistFilesController readSeaDistFilesController = new ReadSeaDistFilesController();
//        readSeaDistFilesController.readCountryFileAndSaveData("countries.csv");
//        readSeaDistFilesController.readBorderFileAndSaveData("borders.csv");
//        ReadStorageFileController readStorageFileController = new ReadStorageFileController();
//        readStorageFileController.readFileAndSaveData("bports.csv");
//        readSeaDistFilesController.readSeaDistFileAndSaveData("seadists.csv");
//        ReadShipFileController readShipFileController = new ReadShipFileController();
//        readShipFileController.readFileAndSaveData("sships.csv");
//    }

    @Test
    public void US301(){
        if (dataBase) {
            CountrySqlStore.loadGraph(MainStorage.getInstance().getDatabaseConnection(), 4);
            StringBuilder sb = new StringBuilder();
            sb.append(MainStorage.getInstance().getPortsGraph().getMg());
            Map<Country, Integer> map = MainStorage.getInstance().getPortsGraph().colourCountries();
            System.out.println(MainStorage.getInstance().getPortsGraph().showColours(map));
            writeOutput(sb.toString(), "US301");
        }
    }

    @Test
    public void US304() {
        if(dataBase) {
            ContainerAuditController controller = new ContainerAuditController();
            String values = controller.getContainerAuditToString(controller.getContainerAudit(1,1));
            StringBuilder sb = new StringBuilder();

            sb.append("Audit log for container\n\n");
            sb.append(values);

            writeOutput(sb.toString(), "US304");
        }
    }

    private void writeOutput(String output, String filename){
        try (FileWriter myWriter = new FileWriter("output\\sprint3\\" + filename + ".txt")) {
            if(output!=null)
                myWriter.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
