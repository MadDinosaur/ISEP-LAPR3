package lapr.project.utils;

import lapr.project.model.Coordinate;
import lapr.project.model.PositioningData;
import lapr.project.model.Ship;
import lapr.project.store.list.PositioningDataList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SorterMeanSogByDateTest {
    SorterMeanSogByDate testSorter;
    Ship s1, s2, s3;
    Date date1, date2;

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
    void compareEqualMMSI() {
        int expected  = 0;
        int result = testSorter.compare(s1, s1);

        assertEquals(expected, result);
    }

    @Test
    void compareEqualDifMMSI() {
        int result = testSorter.compare(s1, s3);

        assertTrue(result < 0);
    }

    @BeforeEach
    void setUp() {
        s1 = new Ship("111111111", "TestShip1", 1111111, "TestCallsign1", 1, 0f, 0f, 0f);
        s2 = new Ship("222222222", "TestShip2", 2222222, "TestCallsign2", 1, 0f, 0f, 0f);
        s3 = new Ship("333333333", "TestShip3", 3333333, "TestCallsign3", 1, 0f, 0f, 0f);

        PositioningData s1p1 = new PositioningData("02/12/2021 00:00", new Coordinate(0,0),10f,0f,0f,"TestPos","A");
        PositioningData s1p2 = new PositioningData("02/12/2021 00:00", new Coordinate(0,10),10f,0f,0f,"TestPos","A");
        PositioningDataList s1Pos = new PositioningDataList();
        s1Pos.insertPositioningDataList(s1p1);
        s1Pos.insertPositioningDataList(s1p2);
        s1.setPositioningDataList(s1Pos);

        PositioningData s2p1 = new PositioningData("02/12/2021 00:00", new Coordinate(0,0),20f,0f,0f,"TestPos","A");
        PositioningData s2p2 = new PositioningData("02/12/2021 00:00", new Coordinate(0,5),20f,0f,0f,"TestPos","A");
        PositioningDataList s2Pos = new PositioningDataList();
        s2Pos.insertPositioningDataList(s2p1);
        s2Pos.insertPositioningDataList(s2p2);
        s2.setPositioningDataList(s2Pos);

        s3.setPositioningDataList(s1Pos);

        date1 = new Date("12/01/2021 00:00");
        date2 = new Date("12/03/2021 00:00");
        testSorter = new SorterMeanSogByDate(date1, date2);
    }
}