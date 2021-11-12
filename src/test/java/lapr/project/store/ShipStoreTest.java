package lapr.project.store;

import lapr.project.model.*;
import lapr.project.store.list.PositioningDataList;
import lapr.project.utils.SorterTraveledDistance;
import lapr.project.utils.SorterTraveledDistByDiff;
import oracle.ucp.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ShipStoreTest {

    PositioningDataList positioningDataList1 = new PositioningDataList();
    PositioningDataList positioningDataList2 = new PositioningDataList();
    PositioningDataList positioningDataList3 = new PositioningDataList();
    PositioningDataList positioningDataList4 = new PositioningDataList();
    PositioningDataList positioningDataList5 = new PositioningDataList();
    PositioningDataList positioningDataList6 = new PositioningDataList();

    ShipStore shipStore = new ShipStore();
    SorterTraveledDistance sorterTraveledDistance = new SorterTraveledDistance();

    Coordinate coordinate, coordinate2, coordinate3, coordinate4, coordinate5, coordinate6, coordinate7, coordinate8, coordinate9;

    PositioningData positioningData, positioningData2, positioningData3, positioningData4, positioningData5, positioningData6, positioningData7, positioningData8, positioningData9;

    Ship s1, s2, s3, s4, s5, s6;

    @BeforeEach
    public void setUp(){

        coordinate = new Coordinate(-66.97001f, 42.97875f);
        coordinate2 = new Coordinate(-66.97243f, 42.92236f);
        coordinate3 = new Coordinate(-66.9759f, 42.7698f);
        coordinate4 = new Coordinate(42.97875f, -66.97001f);
        coordinate5 = new Coordinate(43.22513f, -66.96725f);
        coordinate6 = new Coordinate(-66.95001f, 42.97875f);
        coordinate7 = new Coordinate(-80.97875f, -66.22513f);
        coordinate8 = new Coordinate(-81.61433f, -66.9f);
        coordinate9 = new Coordinate(-82.22513f, -66.97001f);

        positioningData = new PositioningData("31/12/2020 17:20", coordinate, 12.9f, 13.1f, 355, "Sea", "B");
        positioningData2 = new PositioningData("31/12/2020 17:03", coordinate2, 12.5f, 2.4f, 358, "Sea","B");
        positioningData3 = new PositioningData("31/12/2020 16:20", coordinate3, 13.3f, 3.7f,356, "Sea","B");
        positioningData4 = new PositioningData("31/12/2020 16:30", coordinate4, 15.3f, 2.8f,356, "Sea","B");
        positioningData5 = new PositioningData("31/12/2020 16:32", coordinate5, 14.3f, 2.7f,356, "Sea","B");
        positioningData6 = new PositioningData("31/12/2020 17:32", coordinate6, 14.3f, 2.7f,356, "Sea","B");
        positioningData7 = new PositioningData("1/01/2021 17:40", coordinate7, 18.3f, 2.7f,356, "Sea","B");
        positioningData8 = new PositioningData("1/01/2021 17:47", coordinate8, 14.4f, 2.7f,356, "Sea","B");
        positioningData9 = new PositioningData("1/01/2021 17:52", coordinate9, 17.5f, 2.7f,356, "Sea","B");

        positioningDataList1.insertPositioningDataList(positioningData);
        positioningDataList1.insertPositioningDataList(positioningData2);
        positioningDataList1.insertPositioningDataList(positioningData3);

        positioningDataList2.insertPositioningDataList(positioningData4);
        positioningDataList2.insertPositioningDataList(positioningData5);

        positioningDataList3.insertPositioningDataList(positioningData2);
        positioningDataList3.insertPositioningDataList(positioningData5);

        positioningDataList4.insertPositioningDataList(positioningData2);
        positioningDataList4.insertPositioningDataList(positioningData6);

        positioningDataList5.insertPositioningDataList(positioningData);
        positioningDataList5.insertPositioningDataList(positioningData5);

        positioningDataList6.insertPositioningDataList(positioningData7);
        positioningDataList6.insertPositioningDataList(positioningData8);
        positioningDataList6.insertPositioningDataList(positioningData9);


        String shipName = "Example";
        String mmsi1 = "210950000";
        String mmsi2 = "229857000";
        String mmsi3 = "229850001";
        String mmsi4 = "229857001";
        String mmsi5 = "229857005";
        String mmsi6 = "229857007";

        int imo1 = 9450648;
        int imo2 = 9395044;

        String callSign1 = "C4SQ2";
        String callSign2 = "5BBA4";

        int vesselType = 0;
        int vesselType2 = 1;
        float length = 0;
        float width = 0;
        float draft = 0;

        s1 = new Ship(mmsi1,shipName,imo1,callSign1,vesselType,length,width,draft);
        s2 = new Ship(mmsi2,shipName,imo2,callSign2,vesselType,length,width,draft);
        s3 = new Ship(mmsi3,shipName,imo1,callSign1,vesselType,length,width,draft);
        s4 = new Ship(mmsi4,shipName,imo2,callSign2,vesselType2,length,width,draft);
        s5 = new Ship(mmsi5,shipName,imo2,callSign2,vesselType2,length,width,draft);
        s6 = new Ship(mmsi6,shipName,imo2,callSign2,vesselType2,length,width,draft);

        s1.setPositioningDataList(positioningDataList1);
        s2.setPositioningDataList(positioningDataList2);
        s3.setPositioningDataList(positioningDataList3);
        s4.setPositioningDataList(positioningDataList4);
        s5.setPositioningDataList(positioningDataList5);
        s6.setPositioningDataList(positioningDataList6);

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
        SorterTraveledDistance sorterTraveledDistance = new SorterTraveledDistance();

        assertNotNull(shipStore.sortShips(sorterTraveledDistance));
    }

    @Test
    public void shipsSortedTraveledDistanceToStringTest() {

        TreeSet<Ship> treeShips = shipStore.sortShips(sorterTraveledDistance);
        ArrayList<String> result = shipStore.shipsSortedTraveledDistanceToString(treeShips);
        TreeSet<String> expected = new TreeSet<>();
        expected.add("MMSI: 210950000 - Traveled Distance: 23,238516 - Number of Movements: 3");
        expected.add("MMSI: 229857000 - Traveled Distance: 10,722226 - Number of Movements: 2");

        assertEquals(expected.toString().replaceAll(",","."),result.toString().replaceAll(",","."));
    }

    @Test
    public void shipsPairedCloseRoutesToStringTest() {
        //add ships to pair with
        shipStore.addShip(s3);
        shipStore.addShip(s4);

        //expected
        StringBuilder expected = new StringBuilder();
        expected.append("MMSI: 210950000 - Traveled Distance: 23,238516 - Number of Movements: 3");

        //result
        String result = shipStore.shipsPairedCloseRoutesToString(shipStore.getCloseShipRoutes(0,1000,16000));
    }

    @Test
    public void shipsSortedTraveledDistanceToStringSameTraveledDistanceTest(){
        s2.setPositioningDataList(positioningDataList1);

        TreeSet<Ship> treeShips = shipStore.sortShips(sorterTraveledDistance);
        ArrayList<String> result = shipStore.shipsSortedTraveledDistanceToString(treeShips);
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

        HashMap<Ship, TreeSet<Ship>> result = shipStore.getCloseShipRoutes(0,1000,16000);

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

    @Test
    public void getTopNShipsToStringTest() {
        shipStore.addShip(s3);
        shipStore.addShip(s4);
        shipStore.addShip(s5);
        shipStore.addShip(s6);

        Date date1 = new Date("12/30/2020 10:00");
        Date date2 = new Date("12/31/2020 20:00");

        HashMap<Integer, Pair<TreeMap<Ship, Float>, TreeMap<Ship, Double>>> orderedMaps = new HashMap<>();

        shipStore.getOrderedShipsGroupedByVesselType(date1, date2, orderedMaps);

        ArrayList<String> topList = shipStore.getTopNShipsToString(2, date1, date2, orderedMaps);

        ArrayList<String> expectedList = new ArrayList<>();

        expectedList.add("\nTop 2 Ships by Mean Sog between the Dates Wed Dec 30 10:00:00 GMT 2020 and Thu Dec 31 20:00:00 GMT 2020 from the Vessel Type 0:\n" +
                "\t1. Ship MMSI: 229857000 - Mean Sog: 14.8\n" +
                "\t2. Ship MMSI: 229850001 - Mean Sog: 13.4\n" +
                "\t3. Ship MMSI: 210950000 - Mean Sog: 12.900001\n" +
                "Top 2 Ships by Travelled Distance between the Dates Wed Dec 30 10:00:00 GMT 2020 and Thu Dec 31 20:00:00 GMT 2020 from the Vessel Type 0:\n" +
                "\t1. Ship MMSI: 229850001 - Traveled Distance: 15179.810546875\n" +
                "\t2. Ship MMSI: 210950000 - Traveled Distance: 23.238515853881836\n" +
                "\t3. Ship MMSI: 229857000 - Traveled Distance: 10.7222261428833");

        expectedList.add("\nTop 2 Ships by Mean Sog between the Dates Wed Dec 30 10:00:00 GMT 2020 and Thu Dec 31 20:00:00 GMT 2020 from the Vessel Type 1:\n" +
                "\t1. Ship MMSI: 229857005 - Mean Sog: 13.6\n" +
                "\t2. Ship MMSI: 229857001 - Mean Sog: NaN\n" +
                "Top 2 Ships by Travelled Distance between the Dates Wed Dec 30 10:00:00 GMT 2020 and Thu Dec 31 20:00:00 GMT 2020 from the Vessel Type 1:\n" +
                "\t1. Ship MMSI: 229857005 - Traveled Distance: 15185.0068359375\n" +
                "\t2. Ship MMSI: 229857001 - Traveled Distance: 6.529848098754883");

        for (int i = 0; i < topList.size(); i++) {
            assertEquals(expectedList.get(i), topList.get(i));
        }
    }
}