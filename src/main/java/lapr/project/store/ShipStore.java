package lapr.project.store;

import lapr.project.mappers.ShipMapper;
import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.mappers.dto.ShipDTO;
import lapr.project.model.BST;
import lapr.project.model.Ship;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class ShipStore {

    /**
     * A bst tree sorted by the ship's mmsi
     */
    private TreeSet<Ship> mmsiSortedTree;

    /**
     * A bst tree sorted by the ship's imo
     */
    private TreeSet<Ship> imoSortedTree;

    /**
     * A bst tree sorted by the ship's Call Sign
     */
    private TreeSet<Ship> callSignSortedTree;

    /**
     * starts the different trees with different comparators.
     */
    public ShipStore(){
        mmsiSortedTree = new TreeSet<>(new Comparator<Ship>() {
            @Override
            public int compare(Ship o1, Ship o2) {
                return o1.getMmsi().compareTo(o2.getMmsi());
            }
        });

        imoSortedTree = new TreeSet<>(new Comparator<Ship>() {
            @Override
            public int compare(Ship o1, Ship o2) {
                return Integer.compare(o1.getImo(), o2.getImo());
            }
        });

        callSignSortedTree = new TreeSet<>(new Comparator<Ship>() {
            @Override
            public int compare(Ship o1, Ship o2) {
                return o1.getCallSign().compareTo(o2.getCallSign());
            }
        });
    }

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
            if (!existShip(ship)){
                return callSignSortedTree.add(ship) &&  imoSortedTree.add(ship) && mmsiSortedTree.add(ship);
            } else {
                for (Ship otherShip : mmsiSortedTree.descendingSet()) {
                    if (otherShip.getMmsi().equals(ship.getMmsi()) || otherShip.getImo() == ship.getImo() || otherShip.getCallSign().equals(ship.getCallSign()))
                        otherShip.getPositioningDataList().addPositioningDataList(ship.getPositioningDataList().getPositioningDataList());
                }
            }
        }
        return false;
    }

    /**
     * returns true if a the ship is already in the tree
     * @param ship the ship to be analyzed
     * @return returns true if a the ship is already in the tree
     */
    private boolean existShip(Ship ship){
        for (Ship otherShip : mmsiSortedTree.descendingSet()) {
            if (otherShip.getMmsi().equals(ship.getMmsi()) || otherShip.getImo() == ship.getImo() || otherShip.getCallSign().equals(ship.getCallSign()))
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


    private Ship getShipByMmsi(String mmsi){
        return  null;
    }

//    private BST<Ship> shipTree = new BST<>();
//
//    /**
//     * transforms a list of dtos into a list of Ships
//     * @param shipData a map with all the ship information and their positioning data
//     * @return a ship list
//     */
//    public List<Ship> createShip(Map<ShipDTO, List<PositioningDataDTO>> shipData){
//        return ShipMapper.toModel(shipData);
//    }
//
//    /**
//     * ãdd a new ship to the tree, if the ship already exists simply adds the positioning data
//     * @param ship the ship to be added
//     * @return true if the ship or the ship data is added successfully
//     */
//    public boolean addShip(Ship ship){
//        if (validateShip(ship)){
//            if (!existShip(ship)){
//                shipTree.insert(ship);
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * returns true if a the ship is already in the tree
//     * @param ship the ship to be analyzed
//     * @return returns true if a the ship is already in the tree
//     */
//    private boolean existShip(Ship ship){
//        for (Ship otherShip : shipTree.inOrder()) {
//            if (otherShip.getMmsi().equals(ship.getMmsi()) || otherShip.getImo() == ship.getImo() || otherShip.getCallSign().equals(ship.getCallSign())) {
//                otherShip.getPositioningDataList().addPositioningDataList(ship.getPositioningDataList().getPositioningDataList());
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * validates the ship to be added
//     * @param ship a ship
//     * @return return true if the ship is not null
//     */
//    private boolean validateShip(Ship ship){
//        return ship != null;
//    }
}
