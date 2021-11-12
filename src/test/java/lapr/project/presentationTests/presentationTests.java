package lapr.project.presentationTests;

import lapr.project.controller.GetPositionByDateController;
import lapr.project.controller.ReadShipFileController;
import lapr.project.controller.SearchForShipController;
import lapr.project.controller.SendSummaryController;
import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.mappers.dto.ShipDTO;
import lapr.project.model.Ship;
import oracle.ucp.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class presentationTests {

    @BeforeEach
    private void setUp(){

        ReadShipFileController readShipFileController = new ReadShipFileController();
        readShipFileController.readFileAndSaveData("bships.csv");
    }

    @Test
    public void UserStory2IMO(){

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

    @Test
    public void UserStory4ValidMMSICode(){
        SendSummaryController sendSummaryController = new SendSummaryController();
        writeOutput(sendSummaryController.toSummary("211331640"),"US4-MMSICode");
    }

    @Test
    public void UserStory4ValidIMOCode(){
        SendSummaryController sendSummaryController = new SendSummaryController();
        writeOutput(sendSummaryController.toSummary("9193305"),"US4-IMOCode");
    }

    @Test
    public void UserStory4ValidCallSignCode(){
        SendSummaryController sendSummaryController = new SendSummaryController();
        writeOutput(sendSummaryController.toSummary("DHBN"),"US4-CallSignCode");
    }

    @Test
    public void UserStory4InvalidCode(){
        SendSummaryController sendSummaryController = new SendSummaryController();
        writeOutput(sendSummaryController.toSummary("123"),"US4-InvalidCode");
    }

    private void writeOutput(String output, String filename){
        try (FileWriter myWriter = new FileWriter("output\\" + filename + ".txt")) {
            if(output!=null)
            myWriter.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
