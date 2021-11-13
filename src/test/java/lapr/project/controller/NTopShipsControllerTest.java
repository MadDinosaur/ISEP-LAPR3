package lapr.project.controller;

import lapr.project.data.MainStorage;
import lapr.project.model.Coordinate;
import lapr.project.model.PositioningData;
import lapr.project.model.Ship;
import lapr.project.store.ShipStore;
import lapr.project.store.list.PositioningDataTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class NTopShipsControllerTest {
    PositioningDataTree positioningDataTree1 = new PositioningDataTree();
    PositioningDataTree positioningDataTree2 = new PositioningDataTree();
    PositioningDataTree positioningDataTree3 = new PositioningDataTree();
    PositioningDataTree positioningDataTree4 = new PositioningDataTree();
    PositioningDataTree positioningDataTree5 = new PositioningDataTree();
    PositioningDataTree positioningDataTree6 = new PositioningDataTree();

    Coordinate coordinate, coordinate2, coordinate3, coordinate4, coordinate5, coordinate6, coordinate7, coordinate8, coordinate9;

    PositioningData positioningData, positioningData2, positioningData3, positioningData4, positioningData5, positioningData6, positioningData7, positioningData8, positioningData9;

    Ship s1, s2, s3, s4, s5, s6;

    ShipStore shipStore;

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

        positioningDataTree1.insertPositioningDataTree(positioningData);
        positioningDataTree1.insertPositioningDataTree(positioningData2);
        positioningDataTree1.insertPositioningDataTree(positioningData3);

        positioningDataTree2.insertPositioningDataTree(positioningData4);
        positioningDataTree2.insertPositioningDataTree(positioningData5);

        positioningDataTree3.insertPositioningDataTree(positioningData2);
        positioningDataTree3.insertPositioningDataTree(positioningData5);

        positioningDataTree4.insertPositioningDataTree(positioningData2);
        positioningDataTree4.insertPositioningDataTree(positioningData6);

        positioningDataTree5.insertPositioningDataTree(positioningData);
        positioningDataTree5.insertPositioningDataTree(positioningData5);

        positioningDataTree6.insertPositioningDataTree(positioningData7);
        positioningDataTree6.insertPositioningDataTree(positioningData8);
        positioningDataTree6.insertPositioningDataTree(positioningData9);


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

        s1.setPositioningDataList(positioningDataTree1);
        s2.setPositioningDataList(positioningDataTree2);
        s3.setPositioningDataList(positioningDataTree3);
        s4.setPositioningDataList(positioningDataTree4);
        s5.setPositioningDataList(positioningDataTree5);
        s6.setPositioningDataList(positioningDataTree6);

        shipStore = MainStorage.getInstance().getShipStore();
    }

    @Test
    public void getTopNShipsTestSuccess() {
        if (!shipStore.isEmpty()) {
            for (Ship ship : shipStore.inOrder()) {
                shipStore.remove(ship);
            }
        }

        shipStore.addShip(s1);
        shipStore.addShip(s2);
        shipStore.addShip(s3);
        shipStore.addShip(s4);
        shipStore.addShip(s5);
        shipStore.addShip(s6);

        NTopShipsController controller = new NTopShipsController();

        Date date1 = new Date("12/30/2020 10:00");
        Date date2 = new Date("12/31/2020 20:00");

        ArrayList<String> topList = controller.getTopNShips(2, date1, date2);

        ArrayList<String> expectedList = new ArrayList<>();

        expectedList.add("\nTop 2 Ships by Mean Sog between the Dates "+ date1 +" and " + date2 +" from the Vessel Type 0:\n" +
                "\t1. Ship MMSI: 229857000 - Mean Sog: 14.8 KM/H\n" +
                "\t2. Ship MMSI: 229850001 - Mean Sog: 13.4 KM/H\n" +
                "Top 2 Ships by Travelled Distance between the Dates "+ date1 +" and " + date2 +" from the Vessel Type 0:\n" +
                "\t1. Ship MMSI: 229850001 - Traveled Distance: 15180.5400390625 KM\n" +
                "\t2. Ship MMSI: 210950000 - Traveled Distance: 23.239633560180664 KM");

        expectedList.add("\nTop 2 Ships by Mean Sog between the Dates "+ date1 +" and " + date2 +" from the Vessel Type 1:\n" +
                "\t1. Ship MMSI: 229857005 - Mean Sog: 13.6 KM/H\n" +
                "\t2. Ship MMSI: 229857001 - Mean Sog: 13.4 KM/H\n" +
                "Top 2 Ships by Travelled Distance between the Dates "+ date1 +" and " + date2 +" from the Vessel Type 1:\n" +
                "\t1. Ship MMSI: 229857005 - Traveled Distance: 15185.73828125 KM\n" +
                "\t2. Ship MMSI: 229857001 - Traveled Distance: 6.530162334442139 KM");

        assertEquals(expectedList.size(), topList.size());

        for (int i = 0; i < topList.size(); i++) {
            assertEquals(expectedList.get(i).replaceAll(",", "."), topList.get(i).replaceAll(",", "."));
        }
    }

}