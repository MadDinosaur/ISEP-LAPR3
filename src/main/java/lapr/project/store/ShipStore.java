package lapr.project.store;

import lapr.project.mappers.ShipMapper;
import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.mappers.dto.ShipDTO;
import lapr.project.model.Ship;
import lapr.project.utils.ShipSorter;

import java.util.*;

public class ShipStore {

    /**
     * A tree sorted by the ship's mssi values
     */
    private ShipTree mmsiTree = new ShipTree();

    /**
     * A tree sorted by the ship's imo value
     */
    private ShipTree imoTree = new ShipTree();

    /**
     * A tree sorted by the ship's Call Sign value
     */
    private ShipTree callSignTree = new ShipTree();

    /**
     * transforms a list of dtos into a list of Ships
     * @param shipData a map with all the ship information and their positioning data
     * @return a ship list
     */
    public List<Ship> createShip(Map<ShipDTO, List<PositioningDataDTO>> shipData){
        return ShipMapper.toModel(shipData);
    }

    /**
     * ãdd a new ship to the tree, if the ship already exists simply adds the positioning data
     * @param ship the ship to be added
     * @return true if the ship or the ship data is added successfully
     */
    public boolean addShip(Ship ship){
        if (validateShip(ship)){
            imoTree.insertIMO(ship);
            mmsiTree.insertMMSI(ship);
            callSignTree.insertCallSign(ship);
            return true;
        }
        return false;
    }

    /**
     * validates the ship to be added
     * @param ship a ship
     * @return return true if the ship is not null
     */
    private boolean validateShip(Ship ship){
        return ship != null;
    }

    /**
     * returns a ship with a similar mmsi
     * @param code an mmsi code of a possible ship
     * @return returns a ship if it exists
     */
    public Ship getShipByMMSI(String code){
        return mmsiTree.findMMSI(code);
    }

    /**
     * returns a ship with a similar imo
     * @param code an imo code of a possible ship
     * @return returns a ship if it exists
     */
    public Ship getShipByIMO(String code){
        int codeImo;
        try{
            codeImo = Integer.parseInt(code);
        } catch (NumberFormatException e) {
            codeImo = 0;
        }
        return imoTree.findIMO(codeImo);
    }

    /**
     * returns a ship with a similar Call Sign
     * @param code an Call Sign code of a possible ship
     * @return returns a ship if it exists
     */
    public Ship getShipByCallSign(String code){
        return callSignTree.findCallSign(code);
    }


    /**
     * returns a list with all the ships ordered by TraveledDistance (ascending) and NumberOfMovements (descending)
     * @return returns the sorted list
     */
    public ArrayList<Ship> sortShips(){

        ArrayList<Ship> result = new ArrayList<>();
        ShipSorter shipComparator = new ShipSorter();

        mmsiTree.inOrder().forEach(result::add);

        Collections.sort(result,shipComparator);
        Collections.reverse(result);

        return result;
    }

    /**
     *
     */
    public ArrayList<String> shipsToString(){

        ArrayList<String> result = new ArrayList<>();

        return result;
    }
}
