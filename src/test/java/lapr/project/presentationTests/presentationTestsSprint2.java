package lapr.project.presentationTests;

import lapr.project.controller.*;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import lapr.project.data.ShipSqlStore;
import lapr.project.data.StorageSqlStore;
import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.mappers.dto.StorageDTO;
import oracle.ucp.util.Pair;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public void US202(){
        if (!dataBase) {
            ReadStorageFileController readStorageFileController = new ReadStorageFileController();
            readStorageFileController.readFileAndSaveData("bports.csv");
            ReadShipFileController readShipFileController = new ReadShipFileController();
            readShipFileController.readFileAndSaveData("sships.csv");

        } else {
            StorageSqlStore storageSqlStore = new StorageSqlStore();
            MainStorage.getInstance().getStorageStore().addStorageList(storageSqlStore.getStorageDataFromDataBase(MainStorage.getInstance().getDatabaseConnection()));
            ShipSqlStore shipSqlStore = new ShipSqlStore();
            shipSqlStore.loadShips(MainStorage.getInstance().getDatabaseConnection(), MainStorage.getInstance().getShipStore());
        }

        StringBuilder sb = new StringBuilder();

        NearestPortController controller = new NearestPortController();

        String callSign = "D5VK6";
        String date = "31/12/2020 21:20";

        PositioningDataDTO positioningDataDTO = controller.getPositioningData(callSign, date);

        sb.append("POSITION : \n\n");
        sb.append("The Ship \"").append(callSign).append("\" at that given moment is at : Longitude - ").append(positioningDataDTO.getLongitude()).append(" and Latitude - ").append(positioningDataDTO.getLatitude());
        sb.append("\n\n\nNeares Port: \n\n");

        StorageDTO storageDTO = controller.getNearestStorage(Double.parseDouble(positioningDataDTO.getLongitude()), Double.parseDouble(positioningDataDTO.getLatitude()));
        sb.append("ID - ").append(storageDTO.getIdentification()).append("/ Name - ").append(storageDTO.getName()).append("/ Continent - ").append(storageDTO.getContinent())
                .append("/ Country - ").append(storageDTO.getCountry()).append("/ Longitude - ").append(storageDTO.getLongitude()).append("/ Latitude - ").append(storageDTO.getLatitude());

        writeOutput(sb.toString(), "US202");
    }

    @Test
    public void US204(){
        if (dataBase){
            ContainerStatusController controller = new ContainerStatusController();
            String values = controller.getContainerStatusToString(controller.getContainerStatus(2));
            StringBuilder sb = new StringBuilder();

            sb.append("Container in a ship \n\n");
            sb.append(values);

            values = controller.getContainerStatusToString(controller.getContainerStatus(7));

            sb.append("\n\nContainer in a storage \n\n");
            sb.append(values);

            writeOutput(sb.toString(), "US204");
        }
    }

    @Test
    public void US205(){
        if (dataBase){
            ContainerLoadingInfoController controller = new ContainerLoadingInfoController();
            String values = controller.getNextContainerManifestToString(controller.getNextContainerManifest("CC001", 2, false));
            StringBuilder sb = new StringBuilder();

            sb.append("Containers to be offloaded \n\n");
            sb.append(values);

            writeOutput(sb.toString(), "US205");
        }
    }

    @Test
    public void US206(){
        if (dataBase){
            ContainerLoadingInfoController controller = new ContainerLoadingInfoController();
            String values = controller.getNextContainerManifestToString(controller.getNextContainerManifest("CC001", 2, true));
            StringBuilder sb = new StringBuilder();

            sb.append("Containers to be loaded \n\n");
            sb.append(values);

            writeOutput(sb.toString(), "US206");
        }
    }

    @Test
    public void US207(){
        if (dataBase){
            GetManifestInformationController controller = new GetManifestInformationController();
            Pair<Integer, Double> values = controller.findCargoManifests("CC001", 2020);
            StringBuilder sb = new StringBuilder();

            sb.append("YEAR - 2020 \n\n");
            sb.append("Number of manifests transported : ").append(values.get1st()).append("\n");
            sb.append("Average number of containers transported : ").append(values.get2nd()).append("\n");

            values = controller.findCargoManifests("CC001", 2021);

            sb.append("\n\nYEAR - 2021 \n\n");
            sb.append("Number of manifests transported : ").append(values.get1st()).append("\n");
            sb.append("Average number of containers transported : ").append(values.get2nd()).append("\n");

            writeOutput(sb.toString(), "US207");
        }
    }

    @Test
    public void US208(){
        if (dataBase){
            GetOccupancyRateController controller = new GetOccupancyRateController();
            double values = controller.getOccupancyRate(100000001, 1);

            StringBuilder sb = new StringBuilder();

            sb.append("Manifest num - 1 \n\n");
            sb.append("In the given the ship ").append("100000001").append("the occupancy rate of that manifest is : ").append(values);

            values = controller.getOccupancyRate(100000001, 5);

            sb.append("\n\nManifest num - 5 \n\n");
            sb.append("In the given the ship ").append("100000001").append("the occupancy rate of that manifest is : ").append(values);

            writeOutput(sb.toString(), "US208");
        }
    }

    @Test
    public void US209(){
        if (dataBase){
            GetOccupancyRateGivenMomentController controller = new GetOccupancyRateGivenMomentController();
            Pair<String, Double> values = controller.getOccupancyRateGivenMoment(100000001, "2020-05-20 7:59:23");

            StringBuilder sb = new StringBuilder();

            sb.append("Moment - 2020-05-20 7:59:23 \n\n");
            sb.append(values.get1st()).append(" -   Occupancy rate: ").append(values.get2nd());
            values = controller.getOccupancyRateGivenMoment(100000001, "2020-09-9 6:19:45");

            sb.append("\n\nMoment - 2020-09-9 6:19:45 \n\n");
            sb.append(values.get1st()).append(" -   Occupancy rate: ").append(values.get2nd());

            writeOutput(sb.toString(), "US209");
        }
    }

    @Test
    public void US210() {
        if (dataBase){
            DatabaseConnection dbconnection = MainStorage.getInstance().getDatabaseConnection();
            Connection connection = dbconnection.getConnection();

            AvailableShipsOnMondayController controller = new AvailableShipsOnMondayController();
            List<Pair<Integer, Integer>> resultList = controller.getAvailableShipsOnMonday();
            String resultString = controller.AvailableShipsOnMondayToString(resultList);

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("--TABLE SHIPTRIP'S INFO--\n");
            strBuilder.append("+-----------+-------------+------------------+--------------+--------------+--------------+\n");
            strBuilder.append("| SHIP MMSI | ORIGIN_PORT | DESTINATION_PORT | PARTING_DATE | ARRIVAL_DATE |    STATUS    |\n");

            try {
                String sqlCommand = "SELECT * FROM ShipTrip";

                PreparedStatement getShipTripPreparedStatement = connection.prepareStatement(sqlCommand);

                try (ResultSet shipTripResultSet = getShipTripPreparedStatement.executeQuery()) {
                    while (shipTripResultSet.next()) {
                        strBuilder.append("+-----------+-------------+------------------+--------------+--------------+--------------+\n");
                        strBuilder.append("| ").append(String.format("%9s", shipTripResultSet.getInt(1)));
                        strBuilder.append(" | ").append(String.format("%11s", shipTripResultSet.getInt(2)));
                        strBuilder.append(" | ").append(String.format("%16s", shipTripResultSet.getInt(3)));
                        strBuilder.append(" | ").append(String.format("%12s", shipTripResultSet.getDate(4)));
                        strBuilder.append(" | ").append(String.format("%12s", shipTripResultSet.getDate(5)));
                        strBuilder.append(" | ").append(String.format("%12s", shipTripResultSet.getString(6))).append(" |\n");
                    }
                }

                strBuilder.append("+-----------+-------------+------------------+--------------+--------------+--------------+\n");
                strBuilder.append("\n\nAVAILABLE SHIPS NEXT MONDAY:\n");
                strBuilder.append(resultString);
                writeOutput(strBuilder.toString(), "US210");

            } catch (SQLException exception) {
                Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, null, exception);
                dbconnection.registerError(exception);
            }
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
