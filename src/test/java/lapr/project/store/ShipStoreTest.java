package lapr.project.store;

import lapr.project.model.*;
import lapr.project.store.list.PositioningDataList;
import lapr.project.utils.ShipSorter;
import lapr.project.utils.SorterTraveledDistByDiff;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ShipStoreTest {

    PositioningDataList positioningDataList1 = new PositioningDataList();
    PositioningDataList positioningDataList2 = new PositioningDataList();
    PositioningDataList positioningDataList3 = new PositioningDataList();
    PositioningDataList positioningDataList4 = new PositioningDataList();

    ShipStore shipStore = new ShipStore();
    ShipSorter shipSorter = new ShipSorter();

    Coordinate coordinate, coordinate2, coordinate3, coordinate4, coordinate5, coordinate6;

    PositioningData positioningData;
    PositioningData positioningData2;
    PositioningData positioningData3;
    PositioningData positioningData4;
    PositioningData positioningData5;
    PositioningData positioningData6;
    Ship s1, s2, s3, s4;



    @BeforeEach
    public void setUp(){

        coordinate = new Coordinate((float)-66.97001,((float)42.97875));
        coordinate2 = new Coordinate((float)-66.97243, (float)42.92236);
        coordinate3 = new Coordinate((float) -66.9759, (float)42.7698);
        coordinate4 = new Coordinate((float)42.97875, (float) -66.97001);
        coordinate5 = new Coordinate((float)43.22513, (float) -66.96725);
        coordinate6 = new Coordinate((float)-66.95001,((float)42.97875));

        positioningData = new PositioningData("31/12/2020 17:20", coordinate, (float)12.9, (float)13.1, 355, "Sea", "B");
        positioningData2 = new PositioningData("31/12/2020 17:03", coordinate2, (float)12.5, (float)2.4, 358, "Sea","B");
        positioningData3 = new PositioningData("31/12/2020 16:20", coordinate3, (float)13.3, (float)3.7,356, "Sea","B");
        positioningData4 = new PositioningData("31/12/2020 16:30", coordinate4, (float)15.3, (float)2.8,356, "Sea","B");
        positioningData5 = new PositioningData("31/12/2020 16:32", coordinate5, (float)14.3, (float)2.7,356, "Sea","B");
        positioningData6 = new PositioningData("31/12/2020 17:32", coordinate6, (float)14.3, (float)2.7,356, "Sea","B");


        positioningDataList1.insertPositioningDataList(positioningData);
        positioningDataList1.insertPositioningDataList(positioningData2);
        positioningDataList1.insertPositioningDataList(positioningData3);

        positioningDataList2.insertPositioningDataList(positioningData4);
        positioningDataList2.insertPositioningDataList(positioningData5);

        positioningDataList3.insertPositioningDataList(positioningData2);
        positioningDataList3.insertPositioningDataList(positioningData5);

        positioningDataList4.insertPositioningDataList(positioningData2);
        positioningDataList4.insertPositioningDataList(positioningData6);

        String shipName = "Example";
        String mmsi1 = "210950000";
        String mmsi2 = "229857000";
        String mmsi3 = "229850001";
        String mmsi4 = "229857001";

        int imo1 = 9450648;
        int imo2 = 9395044;

        String callSign1 = "C4SQ2";
        String callSign2 = "5BBA4";

        int vesselType = 0;
        float length = 0;
        float width = 0;
        float draft = 0;

        s1 = new Ship(mmsi1,shipName,imo1,callSign1,vesselType,length,width,draft);
        s2 = new Ship(mmsi2,shipName,imo2,callSign2,vesselType,length,width,draft);
        s3 = new Ship(mmsi3,shipName,imo1,callSign1,vesselType,length,width,draft);
        s4 = new Ship(mmsi4,shipName,imo2,callSign2,vesselType,length,width,draft);

        s1.setPositioningDataList(positioningDataList1);
        s2.setPositioningDataList(positioningDataList2);
        s3.setPositioningDataList(positioningDataList3);
        s4.setPositioningDataList(positioningDataList4);

        shipStore.addShip(s1);
        shipStore.addShip(s2);
    }


    @Test
    public void validateTest(){
        ShipStore shipStore = new ShipStore();
        assertFalse(shipStore.addShip(null));
        assertNull(shipStore.getShipByIMO("a"));
        assertNull(shipStore.getShipByCallSign("a"));
    }

    @Test
    public void sortShipsTraveledDistanceTestNotNull(){
        ShipSorter shipSorter = new ShipSorter();

        assertNotNull(shipStore.sortShips(shipSorter));
    }

    @Test
    public void shipsSortedTraveledDistanceToStringTest() {

        TreeSet<Ship> treeShips = shipStore.sortShips(shipSorter);
        TreeSet<String> result = shipStore.shipsSortedTraveledDistanceToString(treeShips);
        TreeSet<String> expected = new TreeSet<>();
        expected.add("MMSI: 210950000 - Traveled Distance: 23,238516 - Number of Movements: 3");
        expected.add("MMSI: 229857000 - Traveled Distance: 10,722226 - Number of Movements: 2");

        assertEquals(expected.toString().replaceAll(",","."),result.toString().replaceAll(",","."));
    }

    @Test
    public void shipsSortedTraveledDistanceToStringSameTraveledDistanceTest(){
        s2.setPositioningDataList(positioningDataList1);

        TreeSet<Ship> treeShips = shipStore.sortShips(shipSorter);
        TreeSet<String> result = shipStore.shipsSortedTraveledDistanceToString(treeShips);
        TreeSet<String> expected = new TreeSet<>();
        expected.add("MMSI: 210950000 - Traveled Distance: 23,238516 - Number of Movements: 3");
        expected.add("MMSI: 229857000 - Traveled Distance: 23,238516 - Number of Movements: 3");

        assertEquals(expected.toString().replaceAll(",","."),result.toString().replaceAll(",","."));

    }

    @Test
    public void getCloseShipRoutes() {
        //add ships to pair with
        shipStore.addShip(s3);
        shipStore.addShip(s4);

        HashMap<Ship, TreeSet<Ship>> result = shipStore.getCloseShipRoutes(0,16000);

        TreeSet<Ship> expected1 = new TreeSet<>(new SorterTraveledDistByDiff(s1.getPositioningDataList().traveledDistance()));
        TreeSet<Ship> expected2 = new TreeSet<>(new SorterTraveledDistByDiff(s3.getPositioningDataList().traveledDistance()));
        TreeSet<Ship> expected3 = new TreeSet<>(new SorterTraveledDistByDiff(s2.getPositioningDataList().traveledDistance()));
        TreeSet<Ship> expected4 = new TreeSet<>(new SorterTraveledDistByDiff(s4.getPositioningDataList().traveledDistance()));

        expected2.add(s1);
        expected3.add(s1);
        expected3.add(s3);
        expected4.add(s1);
        expected4.add(s2);
        expected4.add(s3);

        HashMap<Ship, TreeSet<Ship>> expected = new HashMap<>();
        expected.put(s1, expected1);
        expected.put(s3, expected2);
        expected.put(s2, expected3);
        expected.put(s4, expected4);

        assertEquals(expected.size(), result.size());

        String expectedAsString = expected.keySet().stream()
                .map(key -> key + "=" + shipStore.shipsSortedTraveledDistanceToString(expected.get(key)))
                .collect(Collectors.joining(", ", "{", "}"));
        String resultAsString = result.keySet().stream()
                .map(key -> key.toString() + "=" + shipStore.shipsSortedTraveledDistanceToString(result.get(key)))
                .collect(Collectors.joining(", ", "{", "}"));

        assertEquals(expectedAsString, resultAsString);
    }

}