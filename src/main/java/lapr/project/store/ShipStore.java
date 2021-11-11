package lapr.project.store;

import lapr.project.mappers.ShipMapper;
import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.mappers.dto.ShipDTO;
import lapr.project.model.AVL;
import lapr.project.model.BST;
import lapr.project.model.Ship;
import lapr.project.utils.ShipSorter;

import java.util.*;

public class ShipStore extends AVL<Ship>{

    /**
     * A Map that links the IMO with the MMSI
     */
    private HashMap<Integer, String> IMOMap = new HashMap<>();

    /**
     * A Map that links the CallSign with the MMSI
     */
    private HashMap<String, String> callSignMap = new HashMap<>();

    /**
     * transforms a list of dtos into a list of Ships
     * @param shipData a map with all the ship information and their positioning data
     * @return a ship list
     */
    public List<Ship> createShip(Map<ShipDTO, List<PositioningDataDTO>> shipData){
        return ShipMapper.toModel(shipData);
    }

    /**
     * Ã£dd a new ship to the tree, if the ship already exists simply adds the positioning data
     * @param ship the ship to be added
     * @return true if the ship or the ship data is added successfully
     */
    public boolean addShip(Ship ship){
        if (validateShip(ship)){
            insert(ship);
            callSignMap.put(ship.getCallSign(), ship.getMmsi());
            IMOMap.put(ship.getImo(), ship.getMmsi());
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
        return findMMSI(code);
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

        return findMMSI(IMOMap.get(codeImo));
    }

    /**
     * returns a ship with a similar Call Sign
     * @param code an Call Sign code of a possible ship
     * @return returns a ship if it exists
     */
    public Ship getShipByCallSign(String code){
        return findMMSI(callSignMap.get(code));
    }

    /**
     * searches the tree to try and find a ship with the same mmsi value
     * @param mmsi the mmsi value to be looked after
     * @return a ship with the same mmsi or null
     */
    private Ship findMMSI(String mmsi){
        if (mmsi != null)
            return findMMSI(mmsi, root());
        else
            return null;
    }

    /**
     * searches the tree to try and find a ship with the same mmsi value
     * @param mmsi the mmsi value to be look after
     * @param node the current node being compared
     * @return a ship with the same mmsi or null
     */
    private Ship findMMSI(String mmsi, AVL.Node<Ship> node){
        if (node == null){
            return null;
        }
        if (node.getElement().getMmsi().compareTo(mmsi) == 0){
            return node.getElement();
        } else if (node.getElement().getMmsi().compareTo(mmsi) > 0){
            return findMMSI(mmsi, node.getLeft());
        }else if (node.getElement().getMmsi().compareTo(mmsi) < 0){
            return findMMSI(mmsi, node.getRight());
        }
        return null;
    }

    /**
     * returns a TreeMap with all the ships ordered by Traveled Distance (Descending) and NumberOfMovements (Ascending);
     *
     * @return returns the sorted TreeMap
     */
    public TreeMap<Ship,Integer> sortShips(){

        ShipSorter sorter = new ShipSorter();
        int i = 0;

        TreeMap<Ship,Integer> result = new TreeMap<>(sorter);

        for (Ship ship : inOrder()){
            result.put(ship,i);
            i++;
        }

        return result;
    }

    /**
     * converts a TreeMap to a string Map with all the ships MMSI, Traveled Distance and Number of Movements
     *
     * @param map the TreeMap to be converted
     * @return the string TreeMap
     */
    public TreeMap<Integer,String> shipsToString(TreeMap<Ship,Integer> map){

        int i = 0;
        TreeMap<Integer,String> result = new TreeMap<>();

        for(Ship ship : map.keySet()){

            String mmsi = ship.getMmsi();
            double traveledDistance = ship.getPositioningDataList().traveledDistance();
            int numberMovements = ship.getPositioningDataList().size();

            String string = String.format("MMSI: %s - Traveled Distance: %f - Number of Movements: %s", mmsi, traveledDistance, numberMovements);
            result.put(i,string);
            i++;
        }

        return result;
    }
}