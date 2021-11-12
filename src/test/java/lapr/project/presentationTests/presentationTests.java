package lapr.project.presentationTests;

import lapr.project.controller.GetPositionByDateController;
import lapr.project.controller.ReadShipFileController;
import lapr.project.controller.SearchForShipController;
import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.mappers.dto.ShipDTO;
import oracle.ucp.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class presentationTests {

    @BeforeEach
    private void setUp(){

    }

    @Test
    public void UserStory2IMO(){
        ReadShipFileController readShipFileController = new ReadShipFileController();
        readShipFileController.readFileAndSaveData("bships.csv");

        StringBuilder output = new StringBuilder();

        SearchForShipController searchForShipController = new SearchForShipController();

        Pair<ShipDTO, List<PositioningDataDTO>> ship =  searchForShipController.getShipByIMO("9037769");

        ShipDTO shipDTO = ship.get1st();
        output.append("Ship Found By IMO:\n\n\n\n\n\n\n");

        output.append("MMSI:").append(shipDTO.getMmsi()).append(" Vessel Name:").append(shipDTO.getVesselType()).append(" IMO:")
                .append(shipDTO.getImo()).append(" Call Sign:").append(shipDTO.getCallSign()).append(" Vessel Type:").append(shipDTO.getVesselType())
                .append(" length:").append(shipDTO.getLength()).append(" Width:").append(shipDTO.getWidth()).append(" Draft:").append(shipDTO.getDraft());

        for (PositioningDataDTO po : ship.get2nd())
            output.append("\n\n").append("Base Date Time: ").append(po.getBdt()).append(" Latitude:").append(po.getLatitude()).append(" Longitude:")
                    .append(po.getLongitude()).append(" SOG: ").append(po.getSog()).append(" COG: ").append(po.getCog()).append(" Heading: ")
                    .append(po.getHeading()).append(" Position: ").append(po.getPosition()).append(" TransceiverClass: ").append(po.getTransceiverClass());

        writeOutput(output.toString(), "US2-IMO");
    }

    @Test
    public void UserStory2CallSign(){
        ReadShipFileController readShipFileController = new ReadShipFileController();
        readShipFileController.readFileAndSaveData("bships.csv");

        StringBuilder output = new StringBuilder();

        SearchForShipController searchForShipController = new SearchForShipController();

        Pair<ShipDTO, List<PositioningDataDTO>> ship =  searchForShipController.getShipByIMO("9037769");

        ShipDTO shipDTO = ship.get1st();
        output.append("Ship Found By IMO:\n\n\n\n\n\n\n");

        output.append("MMSI:").append(shipDTO.getMmsi()).append(" Vessel Name:").append(shipDTO.getVesselType()).append(" IMO:")
                .append(shipDTO.getImo()).append(" Call Sign:").append(shipDTO.getCallSign()).append(" Vessel Type:").append(shipDTO.getVesselType())
                .append(" length:").append(shipDTO.getLength()).append(" Width:").append(shipDTO.getWidth()).append(" Draft:").append(shipDTO.getDraft());

        for (PositioningDataDTO po : ship.get2nd())
            output.append("\n\n").append("Base Date Time: ").append(po.getBdt()).append(" Latitude:").append(po.getLatitude()).append(" Longitude:")
                    .append(po.getLongitude()).append(" SOG: ").append(po.getSog()).append(" COG: ").append(po.getCog()).append(" Heading: ")
                    .append(po.getHeading()).append(" Position: ").append(po.getPosition()).append(" TransceiverClass: ").append(po.getTransceiverClass());

        writeOutput(output.toString(), "US2-CallSign");
    }

    @Test
    public void UserStory2MMSI(){
        ReadShipFileController readShipFileController = new ReadShipFileController();
        readShipFileController.readFileAndSaveData("bships.csv");

        StringBuilder output = new StringBuilder();

        SearchForShipController searchForShipController = new SearchForShipController();

        Pair<ShipDTO, List<PositioningDataDTO>> ship =  searchForShipController.getShipByMMSI("211331640");

        ShipDTO shipDTO = ship.get1st();
        output.append("Ship Found By MMSI:\n\n\n\n\n\n\n");

        output.append("MMSI:").append(shipDTO.getMmsi()).append(" Vessel Name:").append(shipDTO.getVesselType()).append(" IMO:")
                .append(shipDTO.getImo()).append(" Call Sign:").append(shipDTO.getCallSign()).append(" Vessel Type:").append(shipDTO.getVesselType())
                .append(" length:").append(shipDTO.getLength()).append(" Width:").append(shipDTO.getWidth()).append(" Draft:").append(shipDTO.getDraft());

        for (PositioningDataDTO po : ship.get2nd())
            output.append("\n\n").append("Base Date Time: ").append(po.getBdt()).append(" Latitude:").append(po.getLatitude()).append(" Longitude:")
                    .append(po.getLongitude()).append(" SOG: ").append(po.getSog()).append(" COG: ").append(po.getCog()).append(" Heading: ")
                    .append(po.getHeading()).append(" Position: ").append(po.getPosition()).append(" TransceiverClass: ").append(po.getTransceiverClass());

        writeOutput(output.toString(), "US2-MMSI");
    }

    @Test
    public void UserStory2InvalidShip(){
        ReadShipFileController readShipFileController = new ReadShipFileController();
        readShipFileController.readFileAndSaveData("bships.csv");

        StringBuilder output = new StringBuilder();

        SearchForShipController searchForShipController = new SearchForShipController();

        Pair<ShipDTO, List<PositioningDataDTO>> ship =  searchForShipController.getShipByMMSI("211335142");
        if (ship != null) {
            ShipDTO shipDTO = ship.get1st();
            output.append("Ship Found By MMSI:\n\n\n\n\n\n\n");

            output.append("MMSI:").append(shipDTO.getMmsi()).append(" Vessel Name:").append(shipDTO.getVesselType()).append(" IMO:")
                    .append(shipDTO.getImo()).append(" Call Sign:").append(shipDTO.getCallSign()).append(" Vessel Type:").append(shipDTO.getVesselType())
                    .append(" length:").append(shipDTO.getLength()).append(" Width:").append(shipDTO.getWidth()).append(" Draft:").append(shipDTO.getDraft());

            for (PositioningDataDTO po : ship.get2nd())
                output.append("\n\n").append("Base Date Time: ").append(po.getBdt()).append(" Latitude:").append(po.getLatitude()).append(" Longitude:")
                        .append(po.getLongitude()).append(" SOG: ").append(po.getSog()).append(" COG: ").append(po.getCog()).append(" Heading: ")
                        .append(po.getHeading()).append(" Position: ").append(po.getPosition()).append(" TransceiverClass: ").append(po.getTransceiverClass());
        }
        writeOutput(output.toString(), "US2-Failed");
    }

    @Test
    public void UserStory3ValidTimeFrame(){
        ReadShipFileController readShipFileController = new ReadShipFileController();
        readShipFileController.readFileAndSaveData("bships.csv");

        StringBuilder output = new StringBuilder();

        GetPositionByDateController getPositionByDateController = new GetPositionByDateController();

        getPositionByDateController.setShipByMMSI("211331640");
        ShipDTO shipDTO = getPositionByDateController.getShip();
        output.append("Ship Found By MMSI:\n\n\n\n\n\n\n");

        output.append("MMSI:").append(shipDTO.getMmsi()).append(" Vessel Name:").append(shipDTO.getVesselType()).append(" IMO:")
                .append(shipDTO.getImo()).append(" Call Sign:").append(shipDTO.getCallSign()).append(" Vessel Type:").append(shipDTO.getVesselType())
                .append(" length:").append(shipDTO.getLength()).append(" Width:").append(shipDTO.getWidth()).append(" Draft:").append(shipDTO.getDraft());

        String initialDate = "31/12/2020 10:39", finalDate = "31/12/2020 21:39";

        output.append("\n\n\nPositions during ").append(initialDate).append(" and ").append(finalDate).append(" :\n\n\n");

        for (PositioningDataDTO po : getPositionByDateController.getPositioningByDate(initialDate, finalDate))
            output.append("\n\n").append("Base Date Time: ").append(po.getBdt()).append(" Latitude:").append(po.getLatitude()).append(" Longitude:")
                    .append(po.getLongitude()).append(" SOG: ").append(po.getSog()).append(" COG: ").append(po.getCog()).append(" Heading: ")
                    .append(po.getHeading()).append(" Position: ").append(po.getPosition()).append(" TransceiverClass: ").append(po.getTransceiverClass());

        writeOutput(output.toString(), "US3-ExistingTimeFrame");
    }

    @Test
    public void UserStory3InvalidTimeFrame(){
        ReadShipFileController readShipFileController = new ReadShipFileController();
        readShipFileController.readFileAndSaveData("bships.csv");

        StringBuilder output = new StringBuilder();

        GetPositionByDateController getPositionByDateController = new GetPositionByDateController();

        getPositionByDateController.setShipByMMSI("211331640");
        ShipDTO shipDTO = getPositionByDateController.getShip();
        output.append("Ship :\n\n\n\n\n\n\n");

        output.append("MMSI:").append(shipDTO.getMmsi()).append(" Vessel Name:").append(shipDTO.getVesselType()).append(" IMO:")
                .append(shipDTO.getImo()).append(" Call Sign:").append(shipDTO.getCallSign()).append(" Vessel Type:").append(shipDTO.getVesselType())
                .append(" length:").append(shipDTO.getLength()).append(" Width:").append(shipDTO.getWidth()).append(" Draft:").append(shipDTO.getDraft());

        String initialDate = "30/12/2020 10:39", finalDate = "30/12/2020 21:39";

        output.append("\n\n\nPositions during ").append(initialDate).append(" and ").append(finalDate).append(" :\n\n\n");

        for (PositioningDataDTO po : getPositionByDateController.getPositioningByDate(initialDate, finalDate))
            output.append("\n\n").append("Base Date Time: ").append(po.getBdt()).append(" Latitude:").append(po.getLatitude()).append(" Longitude:")
                    .append(po.getLongitude()).append(" SOG: ").append(po.getSog()).append(" COG: ").append(po.getCog()).append(" Heading: ")
                    .append(po.getHeading()).append(" Position: ").append(po.getPosition()).append(" TransceiverClass: ").append(po.getTransceiverClass());

        writeOutput(output.toString(), "US3-InvalidTimeFrame");
    }

    private void writeOutput(String output, String filename){
        try (FileWriter myWriter = new FileWriter("output\\" + filename + ".txt")) {
            myWriter.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
