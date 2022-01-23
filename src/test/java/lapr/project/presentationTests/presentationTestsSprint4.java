package lapr.project.presentationTests;

import lapr.project.controller.*;
import lapr.project.data.CountrySqlStore;
import lapr.project.data.MainStorage;
import lapr.project.model.CartesianCoordinate;
import lapr.project.model.Country;
import lapr.project.model.Location;
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

public class presentationTestsSprint4 {

    boolean dataBase = false;

    @Test
    public void US401(){
        if(dataBase){
            CountrySqlStore.loadGraph(MainStorage.getInstance().getDatabaseConnection(), 0);
            MatrixGraph<Location, Double> mg = MainStorage.getInstance().getPortsGraph().getMg();
            CentralityOnPortsController controller = new CentralityOnPortsController();
            StringBuilder s = new StringBuilder();

            List<Pair<Storage,Integer>> list = controller.getCentrality(100);

            for (Pair<Storage, Integer> storageIntegerPair : list)
                s.append(String.format("Storage: %s --> Centrality: %d\n", storageIntegerPair.get1st().getName(), storageIntegerPair.get2nd()));

            writeOutput(s.toString(), "US401");
        }
    }

    @Test
    public void US402a(){

        if (dataBase) {
            CountrySqlStore.loadGraph(MainStorage.getInstance().getDatabaseConnection(),5);
            MatrixGraph<Location, Double> mg = MainStorage.getInstance().getPortsGraph().getMg();
            ShortestPathController controller = new ShortestPathController();
            StringBuilder sb = new StringBuilder();

            Location start = mg.vertex(17);     // Storage 13390: Name - Setubal
            Location end = mg.vertex(90);       // Storage 18454: Name - St Petersburg

            LinkedList<Location> result = controller.shortestLandPath(start,end);

            if (result != null) {
                sb.append("-= Land path Example =-\n");

                for (Location loc : result) {
                    sb.append(loc.toString());
                    sb.append("\n");
                }

                sb.append("Path Distance: ");
                sb.append(Math.round(controller.getPathDistance(result)));
                sb.append("KM");

            } else
                sb.append("There is no land path from ").append(start.toString()).append(" to ").append(end.toString()).append("\n");

            writeOutput(sb.toString(), "US402a");
        }
    }

    @Test
    public void US402b(){

        if (dataBase) {
            CountrySqlStore.loadGraph(MainStorage.getInstance().getDatabaseConnection(),5);
            MatrixGraph<Location, Double> mg = MainStorage.getInstance().getPortsGraph().getMg();
            ShortestPathController controller = new ShortestPathController();
            StringBuilder sb = new StringBuilder();

            Storage start = (Storage) mg.vertex(16);    // Storage 13012: Name - Leixoes
            Storage end = (Storage) mg.vertex(145);     // Storage 29876: Name - Guayaquil

            LinkedList<Location> result = controller.shortestMaritimePath(start,end);

            if (result != null) {
                sb.append("-= Land maritime Example =-\n");

                for (Location loc : result) {
                    sb.append(loc.toString());
                    sb.append("\n");
                }

                sb.append("Path Distance: ");
                sb.append(Math.round(controller.getPathDistance(result)));
                sb.append("KM");

            } else
                sb.append("There is no maritime path from ").append(start.toString()).append(" to ").append(end.toString()).append("\n");

            writeOutput(sb.toString(), "US402b");
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
    public void US412_16() {
        if (dataBase) {
            StringBuilder s = new StringBuilder();
            CalculateContainerResistivityController controller = new CalculateContainerResistivityController();

            String external = "Zinc";
            String median = "Steel";
            String internal = "Fiber-glass";

            double nonRefRes = controller.getResistivity(external, median, internal, 0.03, 0.10, 0.02, 105);

            external = "Stone Wool";
            median = "Steel";
            internal = "Iron";

            double refRes = controller.getResistivity(external, median, internal, 0.07, 0.10, 0.06, 105);

            List<Pair<Integer, Integer>> baseTemp = new ArrayList<>();
            baseTemp.add(new Pair<>(30, 23));
            baseTemp.add(new Pair<>(20, 17));
            baseTemp.add(new Pair<>(50, 29));

            CalculateTripEnergyController controller1 = new CalculateTripEnergyController();

            s.append("Area = 105 m^2").append("\n\nNon Refrigerated Resistivity: ").append(String.format("%.5f", nonRefRes)).append("\n\nRefrigerated Resistivity: ").append(String.format("%.5f", refRes));

            s.append("\n\nFor 50 container refrigerated and 50 container not refrigerated the energy need for a 100 min trip is : ").append(controller1.getEnergy(100, 0, baseTemp, refRes, nonRefRes));

            writeOutput(s.toString(), "US412_16");
        }
    }

    @Test
    public void US418(){
        if(dataBase) {
            CenterOfMassShipController controller = new CenterOfMassShipController();
            StringBuilder s = new StringBuilder();

            List<Double> massTower = new ArrayList<>();
            massTower.add(100000.0);
            massTower.add(100000.0);
            List<Pair<Double, Double>> tower = new ArrayList<>();
            tower.add(new Pair<>(216.6, 29.3));
            tower.add(new Pair<>(84.4,29.3));
            Pair<Double, Double> ship1Coordinates = controller.getCenterOfMass(55000000.0, 399.2, 58.5, massTower,
                    tower);
            s.append("ULCV - Maersk MC Kinney Moller\n");
            s.append("Center of Gravity (Coordinates):\n");
            s.append(String.format("    Xcm: %.2f\n", ship1Coordinates.get1st()));
            s.append(String.format("    Ycm: %.2f\n\n", ship1Coordinates.get2nd()));

            massTower.add(100000.0);
            tower.clear();
            tower.add(new Pair<>(216.6, 29.3));
            Pair<Double, Double> ship2Coordinates = controller.getCenterOfMass(114500000.0, 260.0, 32.0, massTower,
                    tower);
            s.append("Panamax - ANL Tongala\n");
            s.append("Center of Gravity (Coordinates):\n");
            s.append(String.format("    Xcm: %.2f\n", ship2Coordinates.get1st()));
            s.append(String.format("    Ycm: %.2f\n\n", ship2Coordinates.get2nd()));

            massTower.add(100000.0);
            tower.clear();
            tower.add(new Pair<>(9.62, 10.8));
            Pair<Double, Double> ship3Coordinates = controller.getCenterOfMass(2700000.0, 134.65, 21.50, massTower,
                    tower);
            s.append("Feeder - MV Enforcer\n");
            s.append("Center of Gravity (Coordinates):\n");
            s.append(String.format("    Xcm: %.2f\n", ship3Coordinates.get1st()));
            s.append(String.format("    Ycm: %.2f\n\n", ship3Coordinates.get2nd()));

            writeOutput(s.toString(), "US418");
        }
    }

    @Test
    public void US419() {
        int nContainers = 100;
        PositionContainersController controller = new PositionContainersController();
        CenterOfMassShipController controller2 = new CenterOfMassShipController();
        StringBuilder s = new StringBuilder();

        s.append("ULCV - Maersk MC Kinney Moller\n");
        List<Double> massTower = new ArrayList<>();
        massTower.add(100000.0);
        massTower.add(100000.0);
        List<Pair<Double, Double>> tower = new ArrayList<>();
        tower.add(new Pair<>(0.0, 84.39));
        tower.add(new Pair<>(0.0, 241.795));
        List<Pair<Double, Boolean>> shipLength = new ArrayList<>();
        shipLength.add(new Pair<>(84.39, true));
        shipLength.add(new Pair<>(14.515, false));
        shipLength.add(new Pair<>(126.37, true));
        shipLength.add(new Pair<>(14.515, false));
        shipLength.add(new Pair<>(126.37, true));
        shipLength.add(new Pair<>(33.04, false));
        s.append("Container Positions:\n");
        List<CartesianCoordinate<Integer>> containerPositions = controller.getContainerPositions(shipLength, 58.6, 49.49, 55000000.0, massTower, tower, nContainers);
        for (CartesianCoordinate<Integer> containerPosition : containerPositions)
            s.append(containerPosition).append("\t");
        s.append("\nCenter of Gravity Before Loading (Coordinates):\n");
        Pair<Double, Double> centerMassBefore = controller2.getCenterOfMass(55000000.0, 399.2, 58.5, massTower,tower);
        s.append(String.format("(%.2fm, %.2fm)\n", centerMassBefore.get1st(), centerMassBefore.get2nd()));
        s.append("Center of Gravity After Loading (Coordinates):\n");
        Pair<Double, Double> centerMassAfter = controller.getLoadedCenterOfMass(55000000.0, shipLength, 58.5, massTower,tower,500*nContainers, containerPositions);
        s.append(String.format("(%.2fm, %.2fm)\n", centerMassAfter.get1st(), centerMassAfter.get2nd()));

        massTower.clear();
        tower.clear();
        shipLength.clear();

        s.append("\nPanamax - ANL Tongala\n");
        massTower = new ArrayList<>();
        massTower.add(1000000.0);
        tower = new ArrayList<>();
        tower.add(new Pair<>(0.0, 54.13));
        shipLength = new ArrayList<>();
        shipLength.add(new Pair<>(54.13, true));
        shipLength.add(new Pair<>(24.4, false));
        shipLength.add(new Pair<>(181.47, true));
        s.append("Container Positions:\n");
        containerPositions = controller.getContainerPositions(shipLength, 32.25, 23.59, 11450000.0, massTower, tower, nContainers);
        for (CartesianCoordinate<Integer> containerPosition : containerPositions)
            s.append(containerPosition).append("\t");
        s.append("\nCenter of Gravity Before Loading (Coordinates):\n");
        centerMassBefore = controller2.getCenterOfMass(11450000.0, 260.0, 32.25, massTower,tower);
        s.append(String.format("(%.2fm, %.2fm)\n", centerMassBefore.get1st(), centerMassBefore.get2nd()));
        s.append("Center of Gravity After Loading (Coordinates):\n");
        centerMassAfter = controller.getLoadedCenterOfMass(11450000.0, shipLength, 32.25, massTower,tower,500*nContainers, containerPositions);
        s.append(String.format("(%.2fm, %.2fm)\n", centerMassAfter.get1st(), centerMassAfter.get2nd()));

        massTower.clear();
        tower.clear();
        shipLength.clear();

        s.append("\nFeeder - MV Enforcer\n");
        massTower = new ArrayList<>();
        massTower.add(100000.0);
        tower = new ArrayList<>();
        tower.add(new Pair<>(0.0, 0.0));
        shipLength = new ArrayList<>();
        shipLength.add(new Pair<>(15.32, false));
        shipLength.add(new Pair<>(104.31, true));
        shipLength.add(new Pair<>(15.02, false));
        s.append("Container Positions:\n");
        containerPositions = controller.getContainerPositions(shipLength, 21.50, 18.65, 2700000.0, massTower, tower, nContainers);
        for (CartesianCoordinate<Integer> containerPosition : containerPositions)
            s.append(containerPosition).append("\t");
        s.append("\nCenter of Gravity Before Loading (Coordinates):\n");
        centerMassBefore = controller2.getCenterOfMass(2700000.0, 134.65, 21.50, massTower,tower);
        s.append(String.format("(%.2fm, %.2fm)\n", centerMassBefore.get1st(), centerMassBefore.get2nd()));
        s.append("Center of Gravity After Loading (Coordinates):\n");
        centerMassAfter = controller.getLoadedCenterOfMass(2700000.0, shipLength, 21.50, massTower,tower,500*nContainers, containerPositions);
        s.append(String.format("(%.2fm, %.2fm)\n", centerMassAfter.get1st(), centerMassAfter.get2nd()));

        writeOutput(s.toString(),"US419");
    }

    @Test
    public void US420(){
        if(dataBase){
            VesselSinkController controller = new VesselSinkController();
            int nContainers = 300;
            HashMap<String,Double> result = controller.vesselSink(11450.0,260.0,32.25,nContainers);
            StringBuilder sb = new StringBuilder();
            sb.append("For ANL Tongala: \n");
            sb.append("The vessel sunk a total of: ");
            sb.append(result.get("Height"));
            sb.append(" m \n");
            sb.append("Total mass placed: ");
            sb.append(result.get("Container Weight"));
            sb.append(" KG\n");
            sb.append("The pressure was: ");
            sb.append(result.get("Pressure"));
            sb.append(" Pa \n\n");
            result = controller.vesselSink(2700.0,134.65,21.50,nContainers);
            sb.append("For MV Enforcer: \n");
            sb.append("The vessel sunk a total of: ");
            sb.append(result.get("Height"));
            sb.append(" m \n");
            sb.append("Total mass placed: ");
            sb.append(result.get("Container Weight"));
            sb.append(" KG\n");
            sb.append("The pressure was: ");
            sb.append(result.get("Pressure"));
            sb.append(" Pa \n \n");
            result = controller.vesselSink(55000.0,399.2,58.6,nContainers);
            sb.append("For Maersk MC Kinney Moller: \n");
            sb.append("The vessel sunk a total of: ");
            sb.append(result.get("Height"));
            sb.append(" m \n");
            sb.append("Total mass placed: ");
            sb.append(result.get("Container Weight"));
            sb.append(" KG\n");
            sb.append("The pressure was: ");
            sb.append(result.get("Pressure"));
            sb.append(" Pa \n \n");
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
