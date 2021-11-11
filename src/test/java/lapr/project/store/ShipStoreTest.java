package lapr.project.store;

import lapr.project.model.*;
import lapr.project.store.list.PositioningDataList;
import lapr.project.utils.ShipSorter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class ShipStoreTest {

    PositioningDataList positioningDataList = new PositioningDataList();
    ShipStore shipStore = new ShipStore();
    PositioningData positioningData;
    Coordinate coordinate;
    PositioningData positioningData2;
    Coordinate coordinate2;
    PositioningData positioningData3;
    Coordinate coordinate3;

    @BeforeEach
    public void setUp(){

        coordinate = new Coordinate((float)-66.97001,((float)42.97875));
        positioningData = new PositioningData("31/12/2020 17:20", coordinate, (float)12.9, (float)13.1, 355, "Sea", "B");
        positioningDataList.insertPositioningDataList(positioningData);
        coordinate2 = new Coordinate((float)-66.97243, (float)42.92236);
        positioningData2 = new PositioningData("31/12/2020 17:03", coordinate2, (float)12.5, (float)2.4, 358, "Sea","B");
        positioningDataList.insertPositioningDataList(positioningData2);
        coordinate3 = new Coordinate((float) -66.9759, (float)42.7698);
        positioningData3 = new PositioningData("31/12/2020 16:20", coordinate3, (float)13.3, (float)3.7,356, "Sea","B");
        positioningDataList.insertPositioningDataList(positioningData3);


        PositioningDataList pList1 = new PositioningDataList();
        PositioningDataList pList2 = new PositioningDataList();
        ArrayList<PositioningData> pDataList1 = new ArrayList<>();
        ArrayList<PositioningData> pDataList2 = new ArrayList<>();



        String shipName = "Example";
        String mmsi1 = "210950000";
        String mmsi2 = "229857000";

        int imo1 = 9450648;
        int imo2 = 9395044;

        String callSign1 = "C4SQ2";
        String callSign2 = "5BBA4";

        int vesselType = 0;
        float length = 0;
        float width = 0;
        float draft = 0;

        Coordinate c1 = new Coordinate(42.97875f, (float) -66.97001);
        Coordinate c2 = new Coordinate(43.22513f, (float) -66.96725);

        Ship s1 = new Ship(mmsi1,shipName,imo1,callSign1,vesselType,length,width,draft);
        Ship s2 = new Ship(mmsi2,shipName,imo2,callSign2,vesselType,length,width,draft);

        String bdt = "31/12/2000 23:38";
        float sog = 0;
        float cog = 359;
        float heading = 0;
        String position = "95";
        String transceiverClass = "A";

        PositioningData d1 = new PositioningData(bdt,c1,sog,cog,heading,position,transceiverClass);
        PositioningData d2 = new PositioningData(bdt,c2,sog,cog,heading,position,transceiverClass);

        pDataList1.add(d1);
        pDataList2.add(d2);

        for (PositioningData positioningData : pDataList1)
            pList1.insert(positioningData);
        for (PositioningData positioningData : pDataList2)
            pList2.insert(positioningData);

        s1.setPositioningDataList(positioningDataList);
        s2.setPositioningDataList(pList2);



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
        ShipSorter shipSorter = new ShipSorter();


        TreeSet<Ship> treeShips = shipStore.sortShips(shipSorter);
        TreeSet<String> result = shipStore.shipsSortedTraveledDistanceToString(treeShips);
        TreeSet<String> expected = new TreeSet<>();
        expected.add("MMSI: 210950000 - Traveled Distance: 23,238516 - Number of Movements: 3");
        expected.add("MMSI: 229857000 - Traveled Distance: 0,000000 - Number of Movements: 1");

        assertEquals(expected,result);
    }

}