package lapr.project.store;


import lapr.project.mappers.ShipMapper;
import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.mappers.dto.ShipDTO;
import lapr.project.model.*;
import lapr.project.store.list.PositioningDataTree;
import lapr.project.utils.SorterTraveledDistByDiff;
import oracle.ucp.util.Pair;

import java.util.*;

import static java.lang.Math.round;
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
    public Pair<Double, Double> getCenterOfMass(Double massShip, Double length,Double width, List<Double> massTower, List<Pair<Double,Double>> tower){
        double yTotal = 0;
        double yMass = 0;
        double yShip = massShip * (length/2);
        yTotal += yShip;
        yMass += massShip;
        for(int i=0; i<tower.size(); i++){
            double yControl = massTower.get(i) * tower.get(i).get1st();
            yTotal += yControl;
            yMass += massTower.get(i);
        }
        double yCm = yTotal / yMass;

        double xTotal = 0;
        double xMass = 0;
        double xShip = massShip * (width/2);
        xTotal += xShip;
        xMass += massShip;
        for(int i=0; i<tower.size(); i++){
            double xControl = massTower.get(i) * tower.get(i).get2nd();
            xTotal += xControl;
            xMass += massTower.get(i);
        }
        double xCm = xTotal / xMass;
        return new Pair<>(yCm,xCm);
    }

    /**
     * Gets the coordinates of the center of mass of a ship, with a given load.
     * @param massShip The ship's mass
     * @param lengthShip The ship's length
     * @param widthShip The ship's width
     * @param tower The control bridge(s) coordinate(s)
     * @param massTower The control bridge's mass
     * @param centerMassLoadList The load's center of mass coordinates
     * @param massLoad The load's mass
     * @return coordinates of the center of mass
     */
    public Pair<Double, Double> getCenterOfMass(Double massShip, Double lengthShip, Double widthShip, List<Double> massTower, List<Pair<Double,Double>> tower, Double massLoad, List<CartesianCoordinate<Double>> centerMassLoadList) {
        Pair<Double, Double> centerMassShip = getCenterOfMass(massShip, lengthShip, widthShip, massTower, tower);
        for (CartesianCoordinate<Double> centerMassLoad : centerMassLoadList) {
            double xCm = (massShip * centerMassShip.get1st() + massLoad * centerMassLoad.getX()) / (massShip + massLoad);
            double yCm = (massShip * centerMassShip.get2nd() + massLoad * centerMassLoad.getY()) / (massShip + massLoad);

            centerMassShip = new Pair<>(xCm, yCm);
        }
        return centerMassShip;
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

    /**
     * Determines the coordinates to position a given number of containers in a given area of the ship.
     * @param xMax maximum no. of containers widthwise
     * @param yMax maximum no. of containers lengthwise
     * @param zMax maximum no. of containers heightwise
     * @param centerMass the coordinates of the ship's center of mass (in container units)
     * @param nContainers number of containers to position
     * @param coordinates list of coordinates (in container units) to store all the container positions
     */
    public void positionContainers(int xMax, int yMax, int zMax, CartesianCoordinate<Integer> centerMass, int nContainers, List<CartesianCoordinate<Integer>> coordinates) {
        int x, y, z = 0;

        int layer = 0, xDiff, yDiff;
        while (nContainers > 0) {
            for (int i = 0; i <= layer * 2; i++) {
                xDiff = (i % 2 == 0) ? -i / 2 : round(i / 2f);
                for (int j = 0; j <= layer * 2; j++) {
                    yDiff = (j % 2 == 0) ? -j / 2 : round(j / 2f);

                    x = centerMass.getX() + xDiff;
                    if (x > xMax - 1 || x < 0) continue;
                    y = centerMass.getY() + yDiff;
                    if (y > yMax - 1 || y < 0) continue;
                    z = centerMass.getZ();
                    while (coordinates.contains(new CartesianCoordinate<>(x, y, z)))
                        z++;
                    if (z > zMax - 1) continue;

                    coordinates.add(new CartesianCoordinate<>(x, y, z));
                    nContainers--;

                    if (nContainers == 0) return;
                }
            }
            layer++;
        }
    }

    /**
     * Determines the number of containers that can be placed in a single row - widthwise, lengthwise and heightwise
     * @param shipLength a list of the sections of the ship according to whether it can support containers and its respective length
     * @param width the width of the ship
     * @param height the height of the ship
     * @return a coordinate (x, y, z) with the maximum number of container of each axis
     */
    public CartesianCoordinate<Integer> getShipContainerCapacity(List<Pair<Double, Boolean>> shipLength, double width, double height) {
        int yCapacity = 0;
        int xCapacity = (int) (width / (Container.getWidth()));
        int zCapacity = (int) (height / Container.getHeight());

        for(Pair<Double, Boolean> area : shipLength) {
            boolean availableSpace = area.get2nd();
            double availableLength = area.get1st();

            if (availableSpace) yCapacity += (int) (availableLength / Container.getLength());
        }

        return new CartesianCoordinate<>(xCapacity, yCapacity, zCapacity);
    }

    /**
     * Converts a set of (xx, yy) physical coordinates, in meters, into (x, y, z) coordinates, in container units.
     * If the physical coordinate is in a space not destined for containers, the coordinate will be the closest to a container space
     * @param shipLength a list of the sections of the ship according to whether it can support containers and its respective length
     * @param coordinate the physical coordinate to be converted
     * @return the coordinate or null if not possible to convert
     */
    public CartesianCoordinate<Integer> convertCoordinatePhysicalToContainers(List<Pair<Double, Boolean>> shipLength, Pair<Double, Double> coordinate) {
        double widthCoord = coordinate.get1st() - (Container.getWidth() / 2);
        double lengthCoord = coordinate.get2nd() - (Container.getLength() / 2);

        double availableLength = 0;
        double unavailableLength = 0;
        double totalLength = 0;

        for(Pair<Double, Boolean> area : shipLength) {
            boolean availableSpace = area.get2nd();
            if (availableSpace) availableLength += area.get1st();
                    else unavailableLength += area.get1st();
            totalLength += area.get1st();

            if(availableSpace && totalLength >= lengthCoord) {
                int x = (int) (widthCoord / Container.getWidth());
                int y = (int) ((lengthCoord - unavailableLength) / Container.getLength());

                return new CartesianCoordinate<>(x, y, 0);
            }
        }
        return null;
    }

    /**
     * Converts a set of (x, y, z) coordinates, in container units into (xx, yy, zz) physical coordinates, in meters.
     * The physical coordinate represents the geometric center of the container.
     * @param shipLength a list of the sections of the ship according to whether it can support containers and its respective length
     * @param coordinate the coordinate to be converted
     * @return the coordinate or null if not possible to convert
     */
    public CartesianCoordinate<Double> convertCoordinateContainersToPhysical(List<Pair<Double, Boolean>> shipLength, CartesianCoordinate<Integer> coordinate) {
        int availableContainers = 0;
        int totalAvailableContainers = 0;
        double totalLength = 0;

        for(Pair<Double, Boolean> area : shipLength) {
            boolean availableSpace = area.get2nd();
            if (availableSpace) availableContainers = (int) (area.get1st() / Container.getLength());
            else availableContainers = 0;

            if(availableSpace && totalAvailableContainers + availableContainers >= coordinate.getY() + 1) {
                double x = coordinate.getX() * Container.getWidth() + (Container.getWidth() / 2);
                double y = (coordinate.getY() - totalAvailableContainers) * Container.getLength() + totalLength + (Container.getLength() / 2);
                double z = coordinate.getZ() * Container.getHeight() + (Container.getHeight() / 2);
                return new CartesianCoordinate<>(round(x*100)/100.0, round(y*100)/100.0, round(z*100)/100.0);
            }

            totalLength += area.get1st();
            totalAvailableContainers += availableContainers;
        }
        return null;
    }
}