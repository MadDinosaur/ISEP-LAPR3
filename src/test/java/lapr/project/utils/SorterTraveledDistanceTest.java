package lapr.project.utils;

import lapr.project.model.Coordinate;
import lapr.project.model.PositioningData;
import lapr.project.model.Ship;
import lapr.project.store.ShipStore;
import lapr.project.store.list.PositioningDataList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SorterTraveledDistanceTest {

    PositioningDataList positioningDataList1 = new PositioningDataList();
    PositioningDataList positioningDataList2 = new PositioningDataList();

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
    Ship s1;
    Ship s2;



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

        positioningDataList1.insertPositioningDataList(positioningData);
        positioningDataList1.insertPositioningDataList(positioningData2);
        positioningDataList1.insertPositioningDataList(positioningData3);

        positioningDataList2.insertPositioningDataList(positioningData4);
        positioningDataList2.insertPositioningDataList(positioningData5);

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

        s1 = new Ship(mmsi1,shipName,imo1,callSign1,vesselType,length,width,draft);
        s2 = new Ship(mmsi2,shipName,imo2,callSign2,vesselType,length,width,draft);

        s1.setPositioningDataList(positioningDataList1);
        s2.setPositioningDataList(positioningDataList2);

        shipStore.addShip(s1);
        shipStore.addShip(s2);
    }

    @Test
    public void testGreaterThan(){
        s2.setPositioningDataList(positioningDataList1);

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


}
