package lapr.project.presentationTests;

import lapr.project.controller.*;
import lapr.project.data.CountrySqlStore;
import lapr.project.data.MainStorage;
import lapr.project.mappers.dto.UserDTO;
import lapr.project.model.Country;
import lapr.project.store.PortsGraph;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class presentationTestsSprint3 {

    boolean dataBase = true;

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
            writeOutput(sb.toString(), "US301");
        }
    }

    @Test
    public void US302(){
        if (dataBase) {
            CountrySqlStore.loadGraph(MainStorage.getInstance().getDatabaseConnection(), 0);
            StringBuilder sb = new StringBuilder();
            ColourGraphController colourGraphController = new ColourGraphController();
            sb.append(colourGraphController.getCountryAndBorderColours(colourGraphController.getCountryColours()));
            writeOutput(sb.toString(), "US302");
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

    @Test
    public void US305() {
        if (dataBase) {
            ContainerRouteController controller = new ContainerRouteController();
            String values = controller.getContainerRouteToString(controller.getContainerRoute("AA123",3));
            StringBuilder sb = new StringBuilder();

            sb.append("Route log for container\n\n");
            sb.append(values);

            writeOutput(sb.toString(), "US305");
        }
    }

    @Test
    public void US321() {
        if (dataBase) {
            RegisterNewUserController ctrl = new RegisterNewUserController();

            UserDTO userDTO = new UserDTO(null, "José", "ze@gmai.com", "1");

            StringBuilder strBuilder = new StringBuilder();

            strBuilder.append("New user was created!\n")
                    .append("Registration Code: ")
                    .append(ctrl.registerNewUser(userDTO));

            writeOutput(strBuilder.toString(), "US321");
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
