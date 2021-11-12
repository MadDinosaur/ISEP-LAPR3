package lapr.project.store;


import lapr.project.mappers.ShipMapper;
import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.mappers.dto.ShipDTO;
import lapr.project.model.AVL;
import lapr.project.model.Coordinate;
import lapr.project.model.Ship;
import lapr.project.utils.SorterTraveledDistByDate;
import lapr.project.utils.SorterTraveledDistByDiff;
import oracle.ucp.util.Pair;

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
    public TreeSet<Ship> sortShips(Comparator<Ship> sorter){
        TreeSet<Ship> result = new TreeSet<>(sorter);

        for (Ship ship : inOrder()){
            result.add(ship);
        }

        return result;
    }

    /**
     * converts a TreeMap to a String List with all the ships MMSI, Traveled Distance and Number of Movements
     *
     * @param map the TreeMap to be converted
     * @return the string TreeMap
     */
    public ArrayList<String> shipsSortedTraveledDistanceToString(TreeSet<Ship> map){
        ArrayList<String> result = new ArrayList<>();

        for(Ship ship : map){

            String mmsi = ship.getMmsi();
            double traveledDistance = ship.getPositioningDataList().traveledDistance();
            int numberMovements = ship.getPositioningDataList().size();

            String string = String.format("MMSI: %s - Traveled Distance: %f - Number of Movements: %s", mmsi, traveledDistance, numberMovements);
            result.add(string);
        }

        return result;
    }

    public void getTopNShips(int n, Date date1, Date date2, HashMap<Integer, HashMap<Integer, Pair<Ship, Float>>> topNShipsGrouped) {
        HashMap<Integer, Integer> vesselTypeCount = new HashMap<>();
        SorterTraveledDistByDate sorter = new SorterTraveledDistByDate(date1, date2);
        TreeSet<Ship> orderedShipTree = sortShips(sorter);
        int vesselType;

        for (Ship ship : orderedShipTree) {
            vesselType = ship.getVesselType();

            if (!topNShipsGrouped.containsKey(vesselType)){
                HashMap<Integer, Pair<Ship, Float>> topNShips = new HashMap<>();

                vesselTypeCount.put(vesselType, 0);
                topNShipsGrouped.put(vesselType, topNShips);
            }

            Pair<Ship, Float> shipInfo = new Pair<>(ship, ship.getPositioningDataList().getPositionsByDate(date1, date2).meanSog());

            topNShipsGrouped.get(vesselType).put(vesselTypeCount.get(vesselType), shipInfo);

            vesselTypeCount.replace(vesselType, vesselTypeCount.get(vesselType) + 1);
        }

    }

    /**
     * Returns pairs of ships with close departure/arrival coordinates (both no more than 5km apart).
     * Sorted by MMSI of the first ship and decreasing order of the traveled distance difference between the two.
     * @return map</K,V> where
     *          K - first ship (ordered by MMSI),
     *          V - list of ships to pair with the first one, ordered by traveled distance difference
     */
    public HashMap<Ship, TreeSet<Ship>> getCloseShipRoutes(int maxTraveledDist, int distance) {
        HashMap<Ship, TreeSet<Ship>> closeShipRoutes = new HashMap<>();

        Iterator<Ship> i = inOrder().iterator();
        while(i.hasNext()) {
            Ship ship = i.next();
            Coordinate shipDepartureCoord = ship.getPositioningDataList().departureCoordinates();
            Coordinate shipArrivalCoord = ship.getPositioningDataList().arrivalCoordinates();

            TreeSet<Ship> pairShips = new TreeSet<>(new SorterTraveledDistByDiff(ship.getPositioningDataList().traveledDistance()));

            Iterator<Ship> j = inOrder().iterator();
            while(j.hasNext()) {
                Ship pairShip = j.next();
                if (pairShip.equals(ship)) break;
                if (pairShip.getPositioningDataList().traveledDistance() < maxTraveledDist) continue;

                Coordinate pairDepartureCoord = pairShip.getPositioningDataList().departureCoordinates();
                Coordinate pairArrivalCoord = pairShip.getPositioningDataList().arrivalCoordinates();

                if (pairDepartureCoord.getDistanceBetweenCoordinates(shipDepartureCoord) <= distance &&
                pairArrivalCoord.getDistanceBetweenCoordinates(shipArrivalCoord) <= distance)
                    pairShips.add(pairShip);
            }
            closeShipRoutes.put(ship,pairShips);
        }
        return closeShipRoutes;
    }
}