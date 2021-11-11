package lapr.project.store.list;

import lapr.project.model.AVL;
import lapr.project.model.Coordinate;
import lapr.project.model.PositioningData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PositioningDataListTest {
    PositioningDataList positioningDataList = new PositioningDataList();
    PositioningData positioningData;
    Coordinate coordinate;
    PositioningData positioningData2;
    Coordinate coordinate2;
    PositioningData positioningData3;
    Coordinate coordinate3;

    @BeforeEach
    void setUp(){
        AVL<PositioningData> positioning = new AVL<>();
        coordinate = new Coordinate((float)-66.97001,((float)42.97875));
        positioningData = new PositioningData("31/12/2020 17:20", coordinate, (float)12.9, (float)13.1, 355, "Sea", "B");
        positioningDataList.insertPositioningDataList(positioningData);
        coordinate2 = new Coordinate((float)-66.97243, (float)42.92236);
        positioningData2 = new PositioningData("31/12/2020 17:03", coordinate2, (float)12.5, (float)2.4, 358, "Sea","B");
        positioningDataList.insertPositioningDataList(positioningData2);
        coordinate3 = new Coordinate((float) -66.9759, (float)42.7698);
        positioningData3 = new PositioningData("31/12/2020 16:20", coordinate3, (float)13.3, (float)3.7,356, "Sea","B");
        positioningDataList.insertPositioningDataList(positioningData3);
    }

    @Test
    public void validTest(){
        assertNotNull(positioningDataList);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
        assertEquals("31-12-20 16:20", dateFormat.format(positioningDataList.getFirstDate()));
        assertEquals("31-12-20 17:20", dateFormat.format(positioningDataList.getLastDate()));
        assertEquals((float)42.7698,positioningDataList.departureCoordinates().getLatitude());
        assertEquals((float)-66.9759, positioningDataList.departureCoordinates().getLongitude());
        assertEquals((float)42.97875, positioningDataList.arrivalCoordinates().getLatitude());
        assertEquals((float)-66.97001,positioningDataList.arrivalCoordinates().getLongitude());
        assertEquals((float) 3600000.0, positioningDataList.totalMovementTime());
        assertEquals((float) 3, positioningDataList.totalMovementNumber());
        assertEquals((float) 13.3, positioningDataList.maxSog());
        assertEquals((float) 12.900001, positioningDataList.meanSog());
        assertEquals((float) 13.1, positioningDataList.maxCog());
        assertEquals((float) 6.4, positioningDataList.meanCog());
        assertEquals(23.238515853881836,positioningDataList.traveledDistance());
        assertEquals(23.238023488476994, positioningDataList.deltaDistance());
    }

    @Test
    public void invalidTraveledDistance(){
        Coordinate c = new Coordinate((float)-66.97001,((float)42.97875));
        PositioningData p1 = new PositioningData("31/12/2020 17:20", c, (float)12.9, (float)13.1, 355, "Sea", "B");
        PositioningDataList pl1 = new PositioningDataList();
        pl1.insertPositioningDataList(p1);
        assertEquals(0,pl1.traveledDistance());
    }
}