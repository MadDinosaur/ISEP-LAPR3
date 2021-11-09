package lapr.project.store;

import lapr.project.mappers.ShipMapper;
import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.mappers.dto.ShipDTO;
import lapr.project.model.BST;
import lapr.project.model.Ship;
import lapr.project.utils.ShipSorter;

import java.util.*;

public class ShipStore extends BST<Ship>{

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
        return findMMSI(mmsi, root());
    }

    /**
     * searches the tree to try and find a ship with the same mmsi value
     * @param mmsi the mmsi value to be look after
     * @param node the current node being compared
     * @return a ship with the same mmsi or null
     */
    private Ship findMMSI(String mmsi, BST.Node<Ship> node){
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


//    /**
//     * returns a list with all the ships ordered by TraveledDistance (ascending) and NumberOfMovements (descending)
//     * @return returns the sorted list
//     */
//    public ArrayList<Ship> sortShips(){
//
//        ArrayList<Ship> result = new ArrayList<>();
//        ShipSorter shipComparator = new ShipSorter();
//
//        mmsiTree.inOrder().forEach(result::add);
//
//        Collections.sort(result,shipComparator);
//        Collections.reverse(result);
//
//        return result;
//    }

//    /**
//     * converts a string list with all the ships MMSI, Traveled Distance and Number of Movements
//     * @param list the list to be converted
//     * @return the string list
//     */
//    public ArrayList<String> shipsToString(ArrayList<Ship> list){
//
//        ArrayList<String> result = new ArrayList<>();
//        String string;
//
//        for(Ship s : list){
//
//
//            String mmsi = s.getMmsi();
//            double traveledDistance = s.getPositioningDataList().traveledDistance();
//            double numberMovements = s.getPositioningDataList().size();
//
//            string = String.format("MMSI: %s - Traveled Distance: %f - Number of Movements: %d");
//            result.add(string);
//        }
//
//        return result;
//    }
}