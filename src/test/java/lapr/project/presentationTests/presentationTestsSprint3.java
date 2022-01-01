package lapr.project.presentationTests;

import lapr.project.controller.*;
import lapr.project.data.*;
import lapr.project.mappers.dto.UserDTO;
import oracle.ucp.util.Pair;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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
            writeOutput(sb.toString(), "US301");
        }
    }

    @Test
    public void US302(){
        if (dataBase) {
            CountrySqlStore.loadGraph(MainStorage.getInstance().getDatabaseConnection(), 0);
            StringBuilder sb = new StringBuilder();
            ColourGraphController colourGraphController = new ColourGraphController();
            sb.append(colourGraphController.getCountryAndBorderColours());
            writeOutput(sb.toString(), "US302");
        }
    }

    @Test
    public void US303(){
        if (dataBase){
            int n = 10;
            GetPlaceClosestToAllPlacesController controller = new GetPlaceClosestToAllPlacesController();
            HashMap<String, List<String>> result = controller.getPlaceClosestToAllPlaces(n);
            StringBuilder sb = new StringBuilder();

                sb.append("V TOP ");
                sb.append(n);
                sb.append(" CLOSEST PLACES V");
                sb.append("\n");
            for (String s : result.keySet()){
                sb.append("============================================================ ");
                sb.append(s);
                sb.append(" ============================================================");
                sb.append("\n");
                for(String string : result.get(s)){
                    sb.append(">>    ");
                    sb.append(string);
                    sb.append("\n");
                }
            }
            sb.append(" ===============================================================================================================================");
            writeOutput(sb.toString(),"US303");
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
    public void US306(){
        if(dataBase){
            WarehouseOccupancyAndEstimateController controller = new WarehouseOccupancyAndEstimateController();
            Pair<Integer, Double> pair1 = controller.getOccupancyRate(1);
            Pair<Integer, Integer> pair2 = controller.getEstimateLeavingContainers30Days(2);
            StringBuilder s = new StringBuilder();
            s.append(String.format("Occupancy rate from storage: %d %n", pair1.get1st()));
            s.append(String.format("Occupancy Rate: %f%n%n", pair1.get2nd()));
            s.append(String.format("Estimate containers leaving warehouse no %d in 30 days %n", pair2.get1st()));
            s.append(String.format("Number of Leaving Containers %d%n", pair2.get2nd()));
            s.append(String.format("List Of Leaving Containers ID: %n"));
            List<Integer> containerId = controller.getContainers30Days(2);
            for (Integer integer : containerId) {
                s.append(String.format("    Container ID: %d%n", integer));
            }
            writeOutput(s.toString(), "US306");
        }
    }

    @Test
    public void US3017_20() {
        if (dataBase) {
            CalculateContainerResistivityController controller = new CalculateContainerResistivityController();
            StringBuilder s = new StringBuilder();

            String external = "Zinc";
            String median = "Steel";
            String internal = "Fiber-glass";

            s.append("Not Refrigerated Container example\n\n");
            s.append("External Material: ").append(external).append("  Width = 0.03");
            s.append("\nMedian Material: ").append(median).append("  Width = 0.10");
            s.append("\nInternal Material: ").append(external).append("  Width = 0.02");

            s.append("\n\nThermal Resistance = ").append(controller.getResistivityByArea(external, median, internal, 0.03, 0.10, 0.02));

            external = "Stone Wool";
            median = "Steel";
            internal = "Iron";

            s.append("\n\n\n\nRefrigerated Container example\n\n");
            s.append("External Material: ").append(external).append("  Width = 0.07");
            s.append("\nMedian Material: ").append(median).append("  Width = 0.10");
            s.append("\nInternal Material: ").append(external).append("  Width = 0.06");

            s.append("\n\nThermal Resistance = ").append(controller.getResistivityByArea(external, median, internal, 0.07, 0.10, 0.06));

            writeOutput(s.toString(), "US3017_20");
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
