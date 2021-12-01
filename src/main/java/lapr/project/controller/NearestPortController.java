package lapr.project.controller;

import lapr.project.data.MainStorage;
import lapr.project.mappers.PositioningDataMapper;
import lapr.project.mappers.StorageMapper;
import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.mappers.dto.StorageDTO;
import lapr.project.model.PositioningData;
import lapr.project.model.Ship;
import lapr.project.model.Storage;
import lapr.project.store.ShipStore;
import lapr.project.store.StorageStore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NearestPortController {
    /**
     * the current ship store
     */
    private final ShipStore shipStore;

    /**
     * the current storage store
     */
    private final StorageStore storageStore;

    /**
     * The ship whose dates are gonna be searched
     */
    private Ship ship;

    /**
     * Calls the creator with a the current storage instance
     */
    public NearestPortController() {
        this(MainStorage.getInstance());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     *
     * @param mainStorage the storage instance used to store all information
     */
    public NearestPortController(MainStorage mainStorage) {
        this.shipStore = mainStorage.getShipStore();
        this.storageStore = mainStorage.getStorageStore();
    }

    /**
     * Given a call sign and date the method finds the nearest positioning data time-wise
     * @param callSign the call sign used to search the ship
     * @param date the date that is wished
     * @return returns a positioning data
     */
    public PositioningDataDTO getPositioningData(String callSign, String date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date dateFormated = null;
        try {
            dateFormated = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Ship ship = shipStore.getShipByCallSign(callSign);
        if (ship != null) {
            PositioningData positioningData = ship.getPositioningDataList().getNearestTime(dateFormated);
            if (positioningData != null) {
                return PositioningDataMapper.toDTO(positioningData);
            }
        }
        return null;
    }

    /**
     * returns the nearest storage from the given point
     * @param x the x axis value
     * @param y the y axis value
     * @return returns the nearest storage from the given point
     */
    public StorageDTO getNearestStorage(double x, double y){
        Storage storage = storageStore.findNearestNeighbour(x, y);
        if (storage != null)
            return StorageMapper.toDTO(storage);
        else
            return null;
    }
}
