package lapr.project.presentationTests;

import lapr.project.controller.*;
import lapr.project.data.MainStorage;
import lapr.project.data.ShipSqlStore;
import lapr.project.data.StorageSqlStore;
import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.mappers.dto.StorageDTO;
import lapr.project.model.PositioningData;
import oracle.ucp.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
            String values = controller.getContainerStatusToString(2);
            StringBuilder sb = new StringBuilder();

            sb.append("Container in a ship \n\n");
            sb.append(values);

            values = controller.getContainerStatusToString(7);

            sb.append("\n\nContainer in a storage \n\n");
            sb.append(values);

            writeOutput(sb.toString(), "US204");
        }
    }

    @Test
    public void US205(){
        if (dataBase){
            ContainerLoadingInfoController controller = new ContainerLoadingInfoController();
            String values = controller.getNextContainerManifestToString("1", 2, false);
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
            String values = controller.getNextContainerManifestToString("1", 2, true);
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
            Pair<Integer, Integer> values = controller.findCargoManifests(1, 2020);
            StringBuilder sb = new StringBuilder();

            sb.append("YEAR - 2020 \n\n");
            sb.append("Number of manifests transported : ").append(values.get1st()).append("\n");
            sb.append("Average number of containers transported : ").append(values.get2nd()).append("\n");

            values = controller.findCargoManifests(1, 2021);

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

            values = controller.getOccupancyRate(100000001, 11);

            sb.append("\n\nManifest num - 11 \n\n");
            sb.append("In the given the ship ").append("100000001").append("the occupancy rate of that manifest is : ").append(values);

            writeOutput(sb.toString(), "US208");
        }
    }

    @Test
    public void US209() throws ParseException {
        if (dataBase){
            GetOccupancyRateGivenMomentController controller = new GetOccupancyRateGivenMomentController();
            SimpleDateFormat dateFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm:SS");
            Date date = dateFormate.parse("2020-05-20 7:59:23");
            long time = date.getTime();
            Timestamp timestamp = new Timestamp(time);
            Pair<String, Double> values = controller.getOccupancyRateGivenMoment(100000001, timestamp);

            StringBuilder sb = new StringBuilder();

            sb.append("Moment - 2020-05-20 7:59:23 \n\n");
            sb.append(values.get1st()).append(" -   Occupancy rate: ").append(values.get2nd());


            date = dateFormate.parse("2020-09-8 15:45:21");
            time = date.getTime();
            timestamp = new Timestamp(time);
            values = controller.getOccupancyRateGivenMoment(100000001, timestamp);

            sb.append("Moment - 2020-09-8 15:45:21 \n\n");
            sb.append(values.get1st()).append(" -   Occupancy rate: ").append(values.get2nd());

            writeOutput(sb.toString(), "US209");
        }
    }

    @Test
    public void US210() {
        if (dataBase){
            GetOccupancyRateGivenMomentController controller = new GetOccupancyRateGivenMomentController();
            SimpleDateFormat dateFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm:SS");
            Date date = dateFormate.parse("2020-05-20 7:59:23");
            long time = date.getTime();
            Timestamp timestamp = new Timestamp(time);
            Pair<String, Double> values = controller.getOccupancyRateGivenMoment(100000001, timestamp);

            StringBuilder sb = new StringBuilder();

            sb.append("Moment - 2020-05-20 7:59:23 \n\n");
            sb.append(values.get1st()).append(" -   Occupancy rate: ").append(values.get2nd());


            date = dateFormate.parse("2020-09-8 15:45:21");
            time = date.getTime();
            timestamp = new Timestamp(time);
            values = controller.getOccupancyRateGivenMoment(100000001, timestamp);

            sb.append("Moment - 2020-09-8 15:45:21 \n\n");
            sb.append(values.get1st()).append(" -   Occupancy rate: ").append(values.get2nd());

            writeOutput(sb.toString(), "US210");
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
