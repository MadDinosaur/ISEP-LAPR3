package lapr.project.controller;

import lapr.project.mappers.PositioningDataMapper;
import lapr.project.mappers.ShipMapper;
import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.mappers.dto.ShipDTO;
import lapr.project.model.Ship;
import lapr.project.data.MainStorage;
import lapr.project.store.ShipStore;
import lapr.project.store.list.PositioningDataList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class SendSummaryController {

    /**
     * the current ship store
     */
    private final ShipStore shipStore;

    /**
     * Calls the creator with the current storage instance
     */
    public SendSummaryController() {
        this(MainStorage.getInstance());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     * @param storage the storage instance used to store all information
     */
    public SendSummaryController(MainStorage storage) {
        this.shipStore = storage.getShipStore();
    }

    /**
     * returns a map with the ship and it's positioning data
     * @param code code for the ship
     * @return map with all the selected ship information
     */
    public Ship getShipByCodeType(String code){
        Ship shipMMSI = shipStore.getShipByMMSI(code);
        Ship shipIMO = shipStore.getShipByIMO(code);
        Ship shipCallSign = shipStore.getShipByCallSign(code);
        if(shipMMSI != null ){
            return shipMMSI;
        }else if(shipIMO != null){
            return shipIMO;
        }else if(shipCallSign != null){
            return shipCallSign;
        }
        return null;
    }

    /**
     * creates a ship summary and returns it
     * @param code code for the ship the user wants a summary
     * @return summary for the selected ship
     */
    public String toSummary(String code){
        Ship ship = getShipByCodeType(code); // fazer check se null
        if(ship == null){
            return null;
        }
        StringBuilder s = new StringBuilder();
        PositioningDataList positioningData = ship.getPositioningDataList();
        DateFormat dateFormat = new SimpleDateFormat("MM.dd.yy.HH.mm");
        s.append(String.format("Ship's Summary:%n"));
        s.append(String.format("Chosen Identification: %s%n", code));
        s.append(String.format("Vessel Name: %s%n", ship.getShipName()));
        s.append(String.format("Start Base Date Time: %s%n", dateFormat.format(positioningData.getFirstDate())));
        s.append(String.format("End Base Date Time: %s%n", dateFormat.format(positioningData.getLastDate())));
        s.append(String.format("Total Movement Time: %f%n", positioningData.totalMovementTime()));
        s.append(String.format("Total Movement Number: %f%n", positioningData.totalMovementNumber()));
        s.append(String.format("Max SOG: %f%n", positioningData.maxSog()));
        s.append(String.format("Mean SOG: %f%n", positioningData.meanSog()));
        s.append(String.format("Max COG: %f%n", positioningData.maxCog()));
        s.append(String.format("Mean COG: %f%n", positioningData.meanCog()));
        s.append(String.format("Departure Latitude: %f%n", positioningData.departureLatitude()));
        s.append(String.format("Departure Longitude: %f%n", positioningData.departureLongitude()));
        s.append(String.format("Arrival Latitude: %f%n", positioningData.arrivalLatitude()));
        s.append(String.format("Arrival Longitude: %f%n", positioningData.arrivalLongitude()));
        s.append(String.format("Travelled Distance: %f%n", positioningData.traveledDistance()));
        s.append(String.format("Delta Distance: %f%n", positioningData.deltaDistance()));
        return s.toString();
    }
}
