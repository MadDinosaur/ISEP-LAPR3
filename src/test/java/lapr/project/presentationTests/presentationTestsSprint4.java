package lapr.project.presentationTests;

import lapr.project.controller.LongestCycleController;
import lapr.project.data.CountrySqlStore;
import lapr.project.data.MainStorage;
import lapr.project.model.Country;
import lapr.project.model.Location;
import lapr.project.model.Storage;
import lapr.project.model.graph.matrix.MatrixGraph;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class presentationTestsSprint4 {

    boolean dataBase = false;

    @Test
    public void US403(){
        if (dataBase) {
            CountrySqlStore.loadGraph(MainStorage.getInstance().getDatabaseConnection(), 5);
            StringBuilder sb = new StringBuilder();
            LongestCycleController controller = new LongestCycleController();

            MatrixGraph<Location, Double> mg =  MainStorage.getInstance().getPortsGraph().getMg();

            LinkedList<Location> cycle = controller.getLongestCycle(mg.vertex(8));

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
