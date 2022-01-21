package lapr.project.store;


import lapr.project.mappers.ShipMapper;
import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.mappers.dto.ShipDTO;
import lapr.project.model.AVL;
import lapr.project.model.Coordinate;
import lapr.project.model.Ship;
import lapr.project.store.list.PositioningDataTree;
import lapr.project.utils.SorterTraveledDistByDiff;
import oracle.ucp.util.Pair;

import java.util.*;

import static lapr.project.utils.SorterMapByValue.sortByValueDesc;


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
            double deltaDistance = ship.getPositioningDataList().deltaDistance();

            String string = String.format("MMSI: %s - Traveled Distance: %f KM - Number of Movements: %s - Delta Distance %f KM", mmsi, traveledDistance, numberMovements,deltaDistance);
            result.add(string);
        }

        return result;
    }

    /**
     * Populates a HashMap that contains a Pair of LinkedHashMaps with grouped ordered lists of ships (with dynamic information that takes part between 2 dates).
     * The Lists are grouped by Vessel Type and the 1st list (LinkedHashMap) is ordered by the ship's Mean SOG and the 2nd list (LinkedHashMap) is ordered by the ship's travelled distance.
     *
     * @param date1 beginning date
     * @param date2 end date
     * @param orderedMaps a Hashmap where the key is the VesselType, and the Value is a pair that will contain the 2 ordered lists (LinkedHashMaps).
     */
    public void getOrderedShipsGroupedByVesselType(Date date1, Date date2, HashMap<Integer, Pair<LinkedHashMap<Ship, Float>, LinkedHashMap<Ship, Double>>> orderedMaps) {
        LinkedHashMap<Ship, Float> meanSogMap;
        LinkedHashMap<Ship, Double> travDistMap;

        Pair<LinkedHashMap<Ship, Float>, LinkedHashMap<Ship, Double>> linkedMapPair;

        int vesselType;

        for (Ship ship : inOrder()) {
            vesselType = ship.getVesselType();

            if (!orderedMaps.containsKey(vesselType)) {
                meanSogMap = new LinkedHashMap<>();
                travDistMap = new LinkedHashMap<>();

                linkedMapPair = new Pair<>(meanSogMap, travDistMap);

                orderedMaps.put(vesselType, linkedMapPair);
            }

            PositioningDataTree dataTree = ship.getPositioningDataList().getPositionsByDate(date1, date2);

            if(!dataTree.isEmpty()) {
                orderedMaps.get(vesselType).get1st().put(ship, dataTree.meanSog());
                orderedMaps.get(vesselType).get2nd().put(ship, dataTree.traveledDistance());
            }
        }

        orderTops(orderedMaps);
    }

    /**
     * Will order the LinkedHashMaps that contain the tops of ships
     *
     * @param maps a Hashmap where the key is the VesselType, and the Value is a pair that will contain the 2 ordered lists (LinkedHashMaps).
     */
    private void orderTops(HashMap<Integer, Pair<LinkedHashMap<Ship, Float>, LinkedHashMap<Ship, Double>>> maps) {
        Map<Ship, Float> tmpMap1;
        Map<Ship, Double> tmpMap2;

        for (int vessel : maps.keySet()) {

            tmpMap1 = (sortByValueDesc(maps.get(vessel).get1st()));

            maps.get(vessel).get1st().clear();
            maps.get(vessel).get1st().putAll(tmpMap1);

            tmpMap2 = (sortByValueDesc(maps.get(vessel).get2nd()));

            maps.get(vessel).get2nd().clear();
            maps.get(vessel).get2nd().putAll(tmpMap2);
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
    public ArrayList<String> getTopNShipsToString(int n, Date date1, Date date2, HashMap<Integer, Pair<LinkedHashMap<Ship, Float>, LinkedHashMap<Ship, Double>>> orderedMaps) {
        LinkedHashMap <Ship, Float> treeMapMeanSog;
        LinkedHashMap <Ship, Double> treeMapTravDist;
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

            Iterator<Ship> iteratorMapMeanSog = treeMapMeanSog.keySet().iterator();
            Iterator<Ship> iteratorMapTravDist = treeMapTravDist.keySet().iterator();

            if (!iteratorMapMeanSog.hasNext())
                stringTopMeanSog.append("\nNo Ships with data recorded between the Dates ").append(date1).append(" and ").append(date2).append(" from the Vessel Type ").append(vesselType).append("!");
            else {
                stringTopMeanSog.append("\nTop ").append(n).append(" Ships by Mean Sog between the Dates ").append(date1).append(" and ").append(date2).append(" from the Vessel Type ").append(vesselType).append(":");
                stringTopTravDist.append("\nTop ").append(n).append(" Ships by Travelled Distance between the Dates ").append(date1).append(" and ").append(date2).append(" from the Vessel Type ").append(vesselType).append(":");

                while (iteratorMapMeanSog.hasNext()) {
                    Ship ship1 = iteratorMapMeanSog.next();
                    Ship ship2 = iteratorMapTravDist.next();

                    if (count < n) {
                        stringTopMeanSog.append("\n\t").append(count + 1).append(". Ship MMSI: ").append(ship1.getMmsi()).append(" - Mean Sog: ").append(treeMapMeanSog.get(ship1)).append(" KM/H");
                        stringTopTravDist.append("\n\t").append(count + 1).append(". Ship MMSI: ").append(ship2.getMmsi()).append(" - Traveled Distance: ").append(treeMapTravDist.get(ship2)).append(" KM");
                    } else
                        break;

                    count++;
                }

                if (count != n) {
                    stringTopMeanSog.append("\n\t<<No more ships with data between the selected Dates>>");
                    stringTopTravDist.append("\n\t<<No more ships with data between the selected Dates>>");
                }
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
    public HashMap<Ship, TreeMap<Ship, Pair<Double, Double>>> getCloseShipRoutes(int minTraveledDist, int distance) {
        HashMap<Ship, TreeMap<Ship, Pair<Double, Double>>> closeShipRoutes = new HashMap<>();

        for (Ship ship : inOrder()) {
            if (ship.getPositioningDataList().traveledDistance() > minTraveledDist) {
                Coordinate shipDepartureCoord = ship.getPositioningDataList().departureCoordinates();
                Coordinate shipArrivalCoord = ship.getPositioningDataList().arrivalCoordinates();

                TreeMap<Ship, Pair<Double, Double>> pairShips = new TreeMap<>(new SorterTraveledDistByDiff(ship.getPositioningDataList().traveledDistance()));

                for (Ship pairShip : inOrder()) {
                    if (pairShip.equals(ship)) break;
                    if (pairShip.getPositioningDataList().traveledDistance() > minTraveledDist) {

                        Coordinate pairDepartureCoord = pairShip.getPositioningDataList().departureCoordinates();
                        Coordinate pairArrivalCoord = pairShip.getPositioningDataList().arrivalCoordinates();

                        double departureDistance = pairDepartureCoord.getDistanceBetweenCoordinates(shipDepartureCoord);
                        double arrivalDistance = pairArrivalCoord.getDistanceBetweenCoordinates(shipArrivalCoord);

                        if (departureDistance <= distance && arrivalDistance <= distance)
                            pairShips.put(pairShip, new Pair<>(departureDistance, arrivalDistance));
                    }
                }
                closeShipRoutes.put(ship, pairShips);
            }
        }
        return closeShipRoutes;
    }

    /**
     * gets the coordinates of the center of mass of a ship, taking in consideration the ship only has a control bridge
     * @param massShip The ship's mass
     * @param length The ship's length
     * @param width The ship's width
     * @param tower The control bridge coordinate
     * @return coordinates of the center of mass
     */
    public Pair<Double, Double> getCenterOfMass(Double massShip, Double length,Double width, Double massTower, Pair<Double,Double> tower){
        double xShip = massShip * (length/2);
        double xControlBridge = massTower * tower.get1st();
        double xCm = (xShip + xControlBridge) / (massShip + massTower);

        double yShip = massShip * (width/2);
        double yControlBridge = massTower * tower.get2nd();
        double yCm = (yShip + yControlBridge) / (massShip + massTower);

        return new Pair<>(xCm,yCm);
    }

    /**
     * This method returns how much the vessel sunk, the total mass placed and the pressure exerted
     * @param mass The ship's mass
     * @param length The ship's length
     * @param width The ship's width
     * @param nContainers The number of containers in it
     * @return Returns a map with 3 informations: How much the vessel sunk, the total mass placed and the pressure exerted
     */
    public HashMap<String, Double> vesselSink(Double mass,Double length,Double width, int nContainers){
        HashMap<String,Double> result = new HashMap<>();

        final double waterDensity = 1000;                                          // KG/m^3
        final double oneTon = 1000;                                                // KG
        final double gravity = 9.81;// m/s

        double shipUnloadedWeight = mass * oneTon;                                 // KG
        double shipLoadedWeight = shipUnloadedWeight + (nContainers * (oneTon/2)); // KG
        double newImmersiveHeight;                                                 // m
        double immersiveHeight;                                                    // m

        double unloadedVolume = shipUnloadedWeight/waterDensity;                   // m^3
        double loadedVolume = shipLoadedWeight/waterDensity;                       // m^3

        immersiveHeight = unloadedVolume/(length*width);                           // m
        newImmersiveHeight = loadedVolume/(length*width);                          // m

        result.put("Height",newImmersiveHeight-immersiveHeight);
        result.put("Container Weight",nContainers*(oneTon/2));

        double force = (shipLoadedWeight*1000) * gravity;                          // N
        double area = (width*length) + (2*(width*newImmersiveHeight)) + (2*(length*newImmersiveHeight));
        double pressure = force/area;                                               // Pa

        result.put("Pressure",pressure);

        return result;
    }
}