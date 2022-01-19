package lapr.project.presentationTests;

import lapr.project.controller.*;
import lapr.project.data.CountrySqlStore;
import lapr.project.data.MainStorage;
import lapr.project.model.Country;
import lapr.project.model.Location;
import lapr.project.model.Ship;
import lapr.project.model.Storage;
import lapr.project.model.graph.matrix.MatrixGraph;
import oracle.ucp.util.Pair;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class presentationTestsSprint4 {

    boolean dataBase = false;

    @Test
    public void US401(){
        if(dataBase){
            CountrySqlStore.loadGraph(MainStorage.getInstance().getDatabaseConnection(), 0);
            MatrixGraph<Location, Double> mg = MainStorage.getInstance().getPortsGraph().getMg();
            CentralityOnPortsController controller = new CentralityOnPortsController();
            StringBuilder s = new StringBuilder();

            List<Pair<Storage,Integer>> list = new ArrayList<>();
            list = controller.getCentrality(100);

            for(int i=0; i< list.size(); i++){
                s.append(String.format("Storage: %s --> Centrality: %d\n", list.get(i).get1st().getName(), list.get(i).get2nd()));
            }
            writeOutput(s.toString(), "US401");
        }
    }

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

    @Test
    public void US404(){
        if(dataBase){
            GetIdleDaysFleetController controller = new GetIdleDaysFleetController();
            List<String> result = controller.getIdleDays(7);
            StringBuilder sb = new StringBuilder();

            for(String res : result){
                sb.append(res);
                sb.append("\n");
            }
            writeOutput(sb.toString(), "US404");
        }
    }

    @Test
    public void US405(){
        if(dataBase){
            AverageOccupancyRateController controller = new AverageOccupancyRateController();
            StringBuilder s = new StringBuilder();

            s.append("Fleet Manager id = 6\n");
            s.append("Ship Mmsi = 100000001\n");
            s.append("And a Period of Time Between 2019-05-20 and 2022-05-20\n");

            s.append("The selected ship had an average occupancy rate of ");

            s.append(controller.getAverageOccupancyRate(6,100000001,"2019-05-20 7:59:23", "2022-05-20 7:59:23"));

            s.append(" between the selected date");

            writeOutput(s.toString(), "US405");
        }
    }

    @Test
    public void US406(){
        if (dataBase) {
            GetShipTripsThresholdController controller = new GetShipTripsThresholdController();
            StringBuilder sb = new StringBuilder();

            sb.append("For fleet manager id = 6\n\n");

            List<String> result = controller.getTripsBeneathThreshold(6);

            for (String string: result)
                sb.append(string).append("\n");

            sb.append("\nFor fleet manager id = 8\n\n");

            result = controller.getTripsBeneathThreshold(8);

            for (String string: result)
                sb.append(string).append("\n");

            writeOutput(sb.toString(), "US406");
        }
    }

    @Test
    public void US407() {
        if (dataBase) {
            GetLoadingUnloadingMapController controller = new GetLoadingUnloadingMapController();
            String values = controller.getLoadingUnloadingMapToString(controller.getLoadingUnloadingMap("9", "2020-05-19 00:00:00"));
            StringBuilder sb = new StringBuilder();
            sb.append("Loading/Unloading Map for Port Manager no. 9\n");
            sb.append("Week 19-05-2020 to 26-05-2020\n\n");
            sb.append(values);
            writeOutput(sb.toString(), "US407");
        }
    }

    @Test
    public void US420(){
        if(dataBase){
            Ship ship = new Ship("210950000","Example",9450648,"C4SQ2",0,320.04f,33.53f,0);
            VesselSinkController controller = new VesselSinkController();
            HashMap<String,Double> result = controller.vesselSink(ship,300);
            StringBuilder sb = new StringBuilder();
            sb.append("The vessel sunk a total of: ");
            sb.append(result.get("Height"));
            sb.append(" m \n");
            sb.append("Total mass placed: ");
            sb.append(result.get("Container Weight"));
            sb.append(" KG\n");
            sb.append("The pressure was: ");
            sb.append(result.get("Pressure"));
            sb.append(" Pa \n");
            writeOutput(sb.toString(),"US420");
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
