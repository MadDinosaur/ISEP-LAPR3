package lapr.project.store;


import lapr.project.mappers.ShipMapper;
import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.mappers.dto.ShipDTO;
import lapr.project.model.AVL;
import lapr.project.model.Coordinate;
import lapr.project.model.Ship;
import lapr.project.utils.SorterMeanSogByDate;
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

    /**
     * Populates a HashMap that contains a Pair of TreeMaps with grouped ordered lists of ships (with dynamic information that takes part between 2 dates).
     * The Lists are grouped by Vessel Type and the 1st list (TreeMap) is ordered by the ship's Mean SOG and the 2nd list (TreeMap) is ordered by the ship's travelled distance.
     *
     * @param date1 beginning date
     * @param date2 end date
     * @param orderedMaps a Hashmap where the key is the VesselType, and the Value is a pair that will contain the 2 ordered lists (TreeMaps).
     */
    public void getOrderedShipsGroupedByVesselType(Date date1, Date date2, HashMap<Integer, Pair<TreeMap<Ship, Float>, TreeMap<Ship, Double>>> orderedMaps) {
        for (Ship ship : inOrder()) {
            int vesselType = ship.getVesselType();

            if (!orderedMaps.containsKey(vesselType)) {
                SorterTraveledDistByDate sorterDist = new SorterTraveledDistByDate(date1, date2);
                SorterMeanSogByDate sorterMeanSog = new SorterMeanSogByDate(date1, date2);

                TreeMap<Ship, Float> meanSogMap = new TreeMap<>(sorterMeanSog);
                TreeMap<Ship, Double> travDistMap = new TreeMap<>(sorterDist);

                Pair<TreeMap<Ship, Float>, TreeMap<Ship, Double>> treeMapPair = new Pair<>(meanSogMap, travDistMap);
                orderedMaps.put(vesselType, treeMapPair);
            }

            orderedMaps.get(vesselType).get1st().put(ship, ship.getPositioningDataList().getPositionsByDate(date1, date2).meanSog());
            orderedMaps.get(vesselType).get2nd().put(ship, ship.getPositioningDataList().getPositionsByDate(date1, date2).traveledDistance());
        }
    }

    /**
     * Given the Top's size, the 2 dates where the data is collected from and the Hashmap that contains the data, will return an ArrayList of Strings
     * with all the information of the Hashmap ready to be printed.
     *
     * @param n size of the Top
     * @param date1 beginning date
     * @param date2 end date
     * @param orderedMaps a Hashmap where the key is the VesselType, and the Value is a pair that will contain the 2 ordered lists (TreeMaps).
     * @return ArrayList where each String contains the information of 2 Tops (Grouped by Vessel Type).
     */
    public ArrayList<String> getTopNShipsToString(int n, Date date1, Date date2, HashMap<Integer, Pair<TreeMap<Ship, Float>, TreeMap<Ship, Double>>> orderedMaps) {
        TreeMap <Ship, Float> treeMapMeanSog;
        TreeMap <Ship, Double> treeMapTravDist;
        ArrayList<String> topNShips = new ArrayList<>();
        StringBuilder stringTopMeanSog;
        StringBuilder stringTopTravDist;
        int count;

        for (int vesselType : orderedMaps.keySet()) {
            treeMapMeanSog = orderedMaps.get(vesselType).get1st();
            treeMapTravDist = orderedMaps.get(vesselType).get2nd();
            stringTopMeanSog = new StringBuilder();
            stringTopTravDist = new StringBuilder();
            count = 0;

            stringTopMeanSog.append("Top ").append(n).append(" Ships by Mean Sog between the Dates ").append(date1).append(" and ").append(date2).append(" from the Vessel Type ").append(vesselType).append(":");
            stringTopTravDist.append("\n\nTop ").append(n).append(" Ships by Travelled Distance between the Dates ").append(date1).append(" and ").append(date2).append(" from the Vessel Type ").append(vesselType).append(":");

            Iterator<Ship> iteratorMapMeanSog = treeMapMeanSog.keySet().iterator();
            Iterator<Ship> iteratorMapTravDist = treeMapTravDist.keySet().iterator();

            while (iteratorMapMeanSog.hasNext()) {
                Ship ship1 = iteratorMapMeanSog.next();
                Ship ship2 = iteratorMapTravDist.next();

                if (count <= n) {
                    stringTopMeanSog.append("\nShip MMSI: ").append(ship1.getMmsi()).append(" - Mean Sog: ").append(treeMapMeanSog.get(ship1));
                    stringTopTravDist.append("\nShip MMSI: ").append(ship2.getMmsi()).append(" - Traveled Distance: ").append(treeMapTravDist.get(ship2));
                } else
                    break;

                count++;
            }

            topNShips.add(stringTopMeanSog.append(stringTopTravDist).toString());
        }

        return topNShips;
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