package lapr.project.controller;

import lapr.project.exception.IllegalPositioningDataException;
import lapr.project.mappers.PositioningDataMapper;
import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.model.PositioningData;
import lapr.project.model.Ship;
import lapr.project.store.MainStorage;
import lapr.project.store.ShipStore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GetPositionByDateController {

    /**
     * the current ship store
     */
    private final ShipStore shipStore;

    /**
     * The ship whose dates are gonna be searched
     */
    private Ship ship;

    /**
     * Calls the creator with a the current storage instance
     */
    public GetPositionByDateController() {
        this(MainStorage.getInstance());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     *
     * @param mainStorage the storage instance used to store all information
     */
    public GetPositionByDateController(MainStorage mainStorage) {
        this.shipStore = mainStorage.getShipStore();
    }

    /**
     * finds a ship through its mmsi
     * @param mmsi the mmsi code
     * @return true if a ship is found
     */
    public boolean setShipByMMSI(String mmsi){
        this.ship = shipStore.getShipByMMSI(mmsi);
        return ship!=null;
    }

    /**
     * finds a ship though it's imo
     * @param imo the imo code
     * @return true if a ship is found
     */
    public boolean setShipByIMO(String imo){
        this.ship = shipStore.getShipByIMO(imo);
        return ship!=null;
    }

    /**
     *  finds a ship though it's call Sign
     * @param callSign the call sign
     * @return true if a ship is found
     */
    public boolean setShipByCallSign(String callSign){
        this.ship = shipStore.getShipByMMSI(callSign);
        return ship!=null;
    }

    /**
     * Returns a list of the information of the ships data within a boundary of time
     * @param dateInitial the starting date to search in
     * @param dateFinal the ending date to search in
     * @return A list of the information of the ships data within a boundary of time
     */
    public List<PositioningDataDTO> getPositioningByDate(String dateInitial, String dateFinal){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date1 = formatter.parse(dateInitial);
            Date date2 = formatter.parse(dateFinal);
            List<PositioningData> tempList = ship.getPositioningDataList().getPositionsByDate(date1, date2);
            if (tempList != null)
                return PositioningDataMapper.toDTO(tempList);
        } catch (ParseException e){
            e.printStackTrace();
            return null;
        }
        return null;
    }
}