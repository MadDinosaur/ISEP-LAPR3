package lapr.project.utils;

import lapr.project.model.Coordinate;
import lapr.project.model.PositioningData;
import lapr.project.model.Ship;
import lapr.project.store.ShipStore;
import lapr.project.store.list.PositioningDataTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SorterTraveledDistanceTest {

    PositioningDataTree positioningDataTree1 = new PositioningDataTree();
    PositioningDataTree positioningDataTree2 = new PositioningDataTree();
    PositioningDataTree positioningDataTree3 = new PositioningDataTree();
    PositioningDataTree positioningDataTree4 = new PositioningDataTree();
    PositioningDataTree positioningDataTree5 = new PositioningDataTree();

    ShipStore shipStore = new ShipStore();
    SorterTraveledDistance sorterTraveledDistance = new SorterTraveledDistance();

    Coordinate coordinate;
    Coordinate coordinate2;
    Coordinate coordinate3;
    Coordinate coordinate4;
    Coordinate coordinate5;


    PositioningData positioningData;
    PositioningData positioningData2;
    PositioningData positioningData3;
    PositioningData positioningData4;
    PositioningData positioningData5;
    PositioningData positioningData6;
    PositioningData positioningData7;
    PositioningData positioningData8;
    PositioningData positioningData9;
    PositioningData positioningData10;
    PositioningData positioningData11;
    PositioningData positioningData12;

    Ship s1;
    Ship s2;
    Ship s3;
    Ship s4;
    Ship s5;

    @BeforeEach
    public void setUp(){

        coordinate = new Coordinate((float)-66.97001,((float)42.97875));
        coordinate2 = new Coordinate((float)-66.97243, (float)42.92236);
        coordinate3 = new Coordinate((float) -66.9759, (float)42.7698);
        coordinate4 = new Coordinate((float)42.97875, (float) -66.97001);
        coordinate5 = new Coordinate((float)43.22513, (float) -66.96725);

        positioningData = new PositioningData("31/12/2020 17:20", coordinate, (float)12.9, (float)13.1, 355, "Sea", "B");
        positioningData2 = new PositioningData("31/12/2020 17:03", coordinate2, (float)12.5, (float)2.4, 358, "Sea","B");
        positioningData3 = new PositioningData("31/12/2020 16:20", coordinate3, (float)13.3, (float)3.7,356, "Sea","B");
        positioningData4 = new PositioningData("31/12/2020 16:30", coordinate4, (float)15.3, (float)2.8,356, "Sea","B");
        positioningData5 = new PositioningData("31/12/2020 16:32", coordinate5, (float)14.3, (float)2.7,356, "Sea","B");
        positioningData6 = new PositioningData("31/12/2020 16:32", coordinate5, (float)14.3, (float)2.7,356, "Sea","B");
        positioningData7 = new PositioningData("31/12/2020 16:32", coordinate5, (float)14.3, (float)2.7,356, "Sea","B");
        positioningData8 = new PositioningData("31/12/2020 16:32", coordinate5, (float)14.3, (float)2.7,356, "Sea","B");
        positioningData9 = new PositioningData("31/12/2020 16:32", coordinate5, (float)14.3, (float)2.7,356, "Sea","B");
        positioningData10 = new PositioningData("31/12/2020 16:32", coordinate5, (float)14.3, (float)2.7,356, "Sea","B");
        positioningData11 = new PositioningData("31/12/2020 16:32", coordinate5, (float)14.3, (float)2.7,356, "Sea","B");
        positioningData12 = new PositioningData("31/12/2020 16:32", coordinate5, (float)14.3, (float)2.7,356, "Sea","B");

        positioningDataTree1.insertPositioningDataTree(positioningData);
        positioningDataTree1.insertPositioningDataTree(positioningData2);
        positioningDataTree1.insertPositioningDataTree(positioningData3);

        positioningDataTree2.insertPositioningDataTree(positioningData4);
        positioningDataTree2.insertPositioningDataTree(positioningData5);

        positioningDataTree3.insertPositioningDataTree(positioningData6);
        positioningDataTree3.insertPositioningDataTree(positioningData7);

        positioningDataTree4.insertPositioningDataTree(positioningData8);
        positioningDataTree4.insertPositioningDataTree(positioningData9);
        positioningDataTree4.insertPositioningDataTree(positioningData10);

        positioningDataTree5.insertPositioningDataTree(positioningData11);
        positioningDataTree5.insertPositioningDataTree(positioningData12);

        String shipName = "Example";
        String mmsi1 = "210950000";
        String mmsi2 = "229857000";
        String mmsi3 = "234567890";
        String mmsi4 = "321278432";
        String mmsi5 = "128916734";

        int imo1 = 9450648;
        int imo2 = 9395044;
        int imo3 = 9239291;
        int imo4 = 1349320;
        int imo5 = 2571521;

        String callSign1 = "C4SQ2";
        String callSign2 = "5BBA4";
        String callSign3 = "ABC17";
        String callSign4 = "D8IT2";
        String callSign5 = "J0S3N";

        int vesselType = 0;
        float length = 0;
        float width = 0;
        float draft = 0;

        s1 = new Ship(mmsi1,shipName,imo1,callSign1,vesselType,length,width,draft);
        s2 = new Ship(mmsi2,shipName,imo2,callSign2,vesselType,length,width,draft);
        s3 = new Ship(mmsi3,shipName,imo3,callSign3,vesselType,length,width,draft);
        s4 = new Ship(mmsi4,shipName,imo4,callSign4,vesselType,length,width,draft);
        s5 = new Ship(mmsi5,shipName,imo5,callSign5,vesselType,length,width,draft);

        s1.setPositioningDataList(positioningDataTree1);
        s2.setPositioningDataList(positioningDataTree2);
        s3.setPositioningDataList(positioningDataTree3);
        s4.setPositioningDataList(positioningDataTree4);
        s5.setPositioningDataList(positioningDataTree5);

        shipStore.addShip(s1);
        shipStore.addShip(s2);
        shipStore.addShip(s3);
        shipStore.addShip(s4);
        shipStore.addShip(s5);
    }

    @Test
    public void testGreaterThan(){
        s2.setPositioningDataList(positioningDataTree1);

        int expected = -1;
        int result = sorterTraveledDistance.compare(s1,s2);

        assertEquals(expected,result);
    }

    @Test
    public void testLessThan(){

        int expected = 1;
        int result = sorterTraveledDistance.compare(s2,s1);

        assertEquals(expected,result);
    }

    @Test
    public void testNumberMovementsGreater(){
        int expected = 1;
        int result = sorterTraveledDistance.compare(s4,s3);

        assertEquals(expected,result);
    }

    @Test
    public void testNumberMovementsLess(){
        int expected = -1;
        int result = sorterTraveledDistance.compare(s3,s4);

        assertEquals(expected,result);
    }

    @Test
    public void testNumberMovementSame(){
        int expected = 1;
        int result = sorterTraveledDistance.compare(s3,s5);

        assertEquals(expected, result);
    }
}
