package lapr.project.controller;

import lapr.project.model.Ship;
import lapr.project.data.MainStorage;
import lapr.project.store.ShipStore;
import lapr.project.store.list.PositioningDataTree;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


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
        if (shipMMSI != null )
            return shipMMSI;
        Ship shipIMO = shipStore.getShipByIMO(code);
        if (shipIMO != null )
            return shipIMO;
        return shipStore.getShipByCallSign(code);
    }

    /**
     * creates a ship summary and returns it
     * @param code code for the ship the user wants a summary
     * @return summary for the selected ship
     */
    public String toSummary(String code){
        if(code==null){
            return null;
        }
        Ship ship = getShipByCodeType(code);
        if(ship==null){
            return null;
        }
        StringBuilder s = new StringBuilder();
        PositioningDataTree positioningData = ship.getPositioningDataList();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH/mm");
        s.append(String.format("Ship's Summary:%n"));
        s.append(String.format("Chosen Identification: %s%n", code));
        s.append(String.format("Vessel Name: %s%n", ship.getShipName()));
        s.append(String.format("Start Base Date Time: %s%n", dateFormat.format(positioningData.getFirstDate())));
        s.append(String.format("End Base Date Time: %s%n", dateFormat.format(positioningData.getLastDate())));
        s.append(String.format("Total Movement Time: %f min %n", positioningData.totalMovementTime()));
        s.append(String.format("Total Movement Number: %d%n", positioningData.size()));
        s.append(String.format("Max SOG: %f KM/H %n", positioningData.maxSog()));
        s.append(String.format("Mean SOG: %f KM/H %n", positioningData.meanSog()));
        s.append(String.format("Max COG: %fº %n", positioningData.maxCog()));
        s.append(String.format("Mean COG: %fº %n", positioningData.meanCog()));
        s.append(String.format("Departure Latitude: %f%n", positioningData.departureCoordinates().getLongitude()));
        s.append(String.format("Departure Longitude: %f%n", positioningData.departureCoordinates().getLatitude()));
        s.append(String.format("Arrival Latitude: %f%n", positioningData.arrivalCoordinates().getLongitude()));
        s.append(String.format("Arrival Longitude: %f%n", positioningData.arrivalCoordinates().getLatitude()));
        s.append(String.format("Travelled Distance: %f KM %n", positioningData.traveledDistance()));
        s.append(String.format("Delta Distance: %f KM %n", positioningData.deltaDistance()));
        return s.toString();
    }
}
