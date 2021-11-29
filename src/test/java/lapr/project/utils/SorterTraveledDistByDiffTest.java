package lapr.project.utils;

import lapr.project.data.DatabaseConnection;
import lapr.project.model.Coordinate;
import lapr.project.model.PositioningData;
import lapr.project.model.Ship;
import lapr.project.store.list.PositioningDataTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SorterTraveledDistByDiffTest {
    SorterTraveledDistByDiff testSorter;
    Ship s1, s2, s3;

    @Test
    void compareLesser() {
        int expected  = 1;
        int result = testSorter.compare(s1, s2);

        assertEquals(expected, result);
    }

    @Test
    void compareGreater() {
        int expected  = -1;
        int result = testSorter.compare(s2, s1);

        assertEquals(expected, result);
    }

    @Test
    void compareEqual() {
        int expected  = 0;
        int result = testSorter.compare(s1, s1);

        assertEquals(expected, result);

        result = testSorter.compare(s1,s3);

        assertEquals(expected, result);
    }

    @BeforeEach
    void setUp() {
        s1 = new Ship("111111111", "TestShip1", 1111111, "TestCallsign1", 1, 0f, 0f, 0f);
        s2 = new Ship("222222222", "TestShip2", 2222222, "TestCallsign2", 1, 0f, 0f, 0f);
        s3 = new Ship("333333333", "TestShip3", 3333333, "TestCallsign3", 1, 0f, 0f, 0f);

        //Distance Ship1 = 1111km - 1000km = 111km
        PositioningData s1p1 = new PositioningData("01/01/2021 00:00", new Coordinate(0,0),0f,0f,0f,"TestPos","A");
        PositioningData s1p2 = new PositioningData("01/12/2021 00:00", new Coordinate(0,10),0f,0f,0f,"TestPos","A");
        PositioningDataTree s1Pos = new PositioningDataTree();
        s1Pos.insertPositioningDataTree(s1p1);
        s1Pos.insertPositioningDataTree(s1p2);
        s1.setPositioningDataList(s1Pos);

        //Distance Ship2 = 556km - 1000km = 444km
        PositioningData s2p1 = new PositioningData("01/01/2021 00:00", new Coordinate(0,0),0f,0f,0f,"TestPos","A");
        PositioningData s2p2 = new PositioningData("01/12/2021 00:00", new Coordinate(0,5),0f,0f,0f,"TestPos","A");
        PositioningDataTree s2Pos = new PositioningDataTree();
        s2Pos.insertPositioningDataTree(s2p1);
        s2Pos.insertPositioningDataTree(s2p2);
        s2.setPositioningDataList(s2Pos);

        //DistanceShip3 = 1111km - 1000km = 111km
        s3.setPositioningDataList(s1Pos);


        testSorter = new SorterTraveledDistByDiff(1000.0);
    }
}