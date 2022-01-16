package lapr.project.presentationTests;

import lapr.project.controller.LongestCycleController;
import lapr.project.controller.ShortestPathController;
import lapr.project.data.CountrySqlStore;
import lapr.project.data.MainStorage;
import lapr.project.model.Country;
import lapr.project.model.Location;
import lapr.project.model.Storage;
import lapr.project.model.graph.matrix.MatrixGraph;
import lapr.project.store.PortsGraph;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class presentationTestsSprint4 {

    boolean dataBase = true;

    @Test
    public void US402d(){

        if (dataBase) {
            CountrySqlStore.loadGraph(MainStorage.getInstance().getDatabaseConnection(),5);
            MatrixGraph<Location, Double> mg = MainStorage.getInstance().getPortsGraph().getMg();
            ShortestPathController controller = new ShortestPathController();
            StringBuilder sb = new StringBuilder();

            LinkedList<Location> locations = new LinkedList<>();
            locations.add(mg.vertex(100));  // Continent - Europe; Country - Finland
            locations.add(mg.vertex(119));  // Continent - America; Country - Nicaragua
            locations.add(mg.vertex(120));  // Continent - America; Country - El Salvador
            locations.add(mg.vertex(14));   // Storage 21863: Name - Aspropyrgos
            locations.add(mg.vertex(148));  // Storage 28313: Name - Cartagena
            Location start = mg.vertex(16); // Storage 13012: Name - Leixoes
            Location end = mg.vertex(9);    // Storage 10136: Name - Larnaca

            ArrayList<Location> mandatoryLocations = new ArrayList<>();
            mandatoryLocations.add(mg.vertex(100));  // Continent - Europe; Country - Finland
            mandatoryLocations.add(mg.vertex(119));  // Continent - America; Country - Nicaragua
            mandatoryLocations.add(mg.vertex(120));  // Continent - America; Country - El Salvador
            mandatoryLocations.add(mg.vertex(14));   // Storage 21863: Name - Aspropyrgos
            mandatoryLocations.add(mg.vertex(148));  // Storage 28313: Name - Cartagena
            mandatoryLocations.add(mg.vertex(16));   // Storage 21863: Name - Aspropyrgos
            mandatoryLocations.add(mg.vertex(9));  // Storage 28313: Name - Cartagena


            LinkedList<Location> result = controller.getShortestPathN(locations,start,end);
            for (Location loc : result){
                if((mandatoryLocations.contains(loc))) sb.append("|MANDATORY| ");
                else sb.append("\t \t \t");
                sb.append(loc.toString());
                sb.append("\n");
            }

            sb.append("Path Distance: ");
            sb.append(Math.round(controller.getPathDistance(result)));
            sb.append("KM");
            writeOutput(sb.toString(), "US402d");
      }
    }

    @Test
    public void US402c(){

        if (dataBase) {
            CountrySqlStore.loadGraph(MainStorage.getInstance().getDatabaseConnection(),5);
            MatrixGraph<Location, Double> mg = MainStorage.getInstance().getPortsGraph().getMg();
            ShortestPathController controller = new ShortestPathController();
            StringBuilder sb = new StringBuilder();

            Location start = mg.vertex(16); // Storage 13012: Name - Leixoes
            Location end = mg.vertex(148);    // Storage 10136: Name - Larnaca

            LinkedList<Location> result = controller.landOrSeaPath(start,end);
            for (Location loc : result){
                sb.append(loc.toString());
                sb.append("\n");
            }

            sb.append("Path Distance: ");
            sb.append(Math.round(controller.getPathDistance(result)));
            sb.append("KM");
            writeOutput(sb.toString(), "US402c");
        }
    }

    @Test
    public void US403(){
        if (dataBase) {
            CountrySqlStore.loadGraph(MainStorage.getInstance().getDatabaseConnection(), 5);
            StringBuilder sb = new StringBuilder();
            LongestCycleController controller = new LongestCycleController();

            MatrixGraph<Location, Double> mg =  MainStorage.getInstance().getPortsGraph().getMg();

            LinkedList<Location> cycle = controller.getLongestCycle(mg.vertex(16));

            sb.append("Total traveled Distance: ").append(String.format("%.0f", controller.getCycleDistance(cycle))).append("\n");
            sb.append("Number of movements: ").append(cycle.size() - 1).append("\n");

            for (int i = 0; i < cycle.size()-1; i++){

                Location location1 = cycle.get(i);
                Location location2 = cycle.get(i+1);
                if (location1 instanceof Country)
                    sb.append( "Country: ").append(location1.getCountry()).append( " || Capital: ").append(((Country) location1).getCapital());
                else
                    sb.append( "Country: ").append(location1.getCountry()).append( " || Port: ").append(((Storage) location1).getIdentification());

                sb.append("  --->  ");

                if (location2 instanceof Country)
                    sb.append( "Country: ").append(location2.getCountry()).append( " || Capital: ").append(((Country) location2).getCapital());
                else
                    sb.append( "Country: ").append(location2.getCountry()).append( " || Port: ").append(((Storage) location2).getIdentification());

                sb.append("\n").append("Movement distance: ").append(String.format("%.0f", mg.edge(location1, location2).getWeight())).append("\n");
            }
            writeOutput(sb.toString(), "US403");
        }
    }

    private void writeOutput(String output, String filename){
        try (FileWriter myWriter = new FileWriter("output\\sprint4\\" + filename + ".txt")) {
            if(output!=null)
                myWriter.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
