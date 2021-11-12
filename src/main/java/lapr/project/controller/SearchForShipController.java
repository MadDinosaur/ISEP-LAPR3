package lapr.project.controller;


import lapr.project.mappers.PositioningDataMapper;
import lapr.project.mappers.ShipMapper;
import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.mappers.dto.ShipDTO;
import lapr.project.model.PositioningData;
import lapr.project.model.Ship;
import lapr.project.store.ShipStore;
import lapr.project.data.MainStorage;
import oracle.ucp.util.Pair;


import java.util.List;


public class SearchForShipController {

    /**
     * the current ship store
     */
    private final ShipStore shipStore;

    /**
     * Calls the creator with a the current storage instance
     */
    public SearchForShipController() {
        this(MainStorage.getInstance());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     *
     * @param mainStorage the storage instance used to store all information
     */
    public SearchForShipController(MainStorage mainStorage) {
        this.shipStore = mainStorage.getShipStore();
    }

    /**
     * Searches for a ship with this mmsi value
     * @param mmsi the mmsi value to search for
     * @return returns all the ship's information and it's dynamic Data
     */
    public Pair<ShipDTO, List<PositioningDataDTO>> getShipByMMSI(String mmsi){
        Ship ship = shipStore.getShipByMMSI(mmsi);
        if (ship != null) {
            return new Pair<>(ShipMapper.toDTO(ship), PositioningDataMapper.toDTO((List<PositioningData>) ship.getPositioningDataList().inOrder()));
        } else
            return null;
    }

    /**
     * Searches for a ship with this imo value
     * @param imo the imo value to be searched
     * @return returns all the ship's information
     */
    public Pair<ShipDTO, List<PositioningDataDTO>> getShipByIMO(String imo){
        Ship ship = shipStore.getShipByIMO(imo);
        if (ship != null) {
            return new Pair<>(ShipMapper.toDTO(ship), PositioningDataMapper.toDTO((List<PositioningData>) ship.getPositioningDataList().inOrder()));
        } else
            return null;
    }

    /**
     * Searches for a ship with this call sign
     * @param callSign the call sign to be searched
     * @return
     */
    public Pair<ShipDTO, List<PositioningDataDTO>> getShipByCallSign(String callSign){
        Ship ship = shipStore.getShipByCallSign(callSign);
        if (ship != null) {
            return new Pair<>(ShipMapper.toDTO(ship), PositioningDataMapper.toDTO((List<PositioningData>) ship.getPositioningDataList().inOrder()));
        } else
            return null;
    }
}
