package lapr.project.controller;

import lapr.project.mappers.ShipMapper;
import lapr.project.mappers.dto.ShipDTO;
import lapr.project.model.Ship;
import lapr.project.store.ShipStore;
import lapr.project.store.Storage;

public class SearchForShipController {

    /**
     * the current ship store
     */
    private final ShipStore shipStore;

    /**
     * Calls the creator with a the current storage instance
     */
    public SearchForShipController() {
        this(Storage.getInstance());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     *
     * @param storage the storage instance used to store all information
     */
    public SearchForShipController(Storage storage) {
        this.shipStore = storage.getShipStore();
    }

    public ShipDTO getShipByMMSI(String mmsi){
        Ship ship = shipStore.getShipByMMSI(mmsi);
        if (ship != null)
            return ShipMapper.toDTO(ship);
        else
            return null;
    }

    public ShipDTO getShipByIMO(String imo){
        Ship ship = shipStore.getShipByIMO(imo);
        if (ship != null)
            return ShipMapper.toDTO(ship);
        else
            return null;
    }

    public ShipDTO getShipByCallSign(String callSign){
        Ship ship = shipStore.getShipByCallSign(callSign);
        if (ship != null)
            return ShipMapper.toDTO(ship);
        else
            return null;
    }
}