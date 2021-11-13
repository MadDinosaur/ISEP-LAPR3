package lapr.project.store.list;

import lapr.project.model.AVL;
import lapr.project.model.Coordinate;
import lapr.project.model.PositioningData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class PositioningDataTreeTest {
    PositioningDataTree positioningDataTree = new PositioningDataTree();
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
        positioningDataTree.insertPositioningDataTree(positioningData);
        coordinate2 = new Coordinate((float)-66.97243, (float)42.92236);
        positioningData2 = new PositioningData("31/12/2020 17:03", coordinate2, (float)12.5, (float)2.4, 358, "Sea","B");
        positioningDataTree.insertPositioningDataTree(positioningData2);
        coordinate3 = new Coordinate((float) -66.9759, (float)42.7698);
        positioningData3 = new PositioningData("31/12/2020 16:20", coordinate3, (float)13.3, (float)3.7,356, "Sea","B");
        positioningDataTree.insertPositioningDataTree(positioningData3);
    }

    @Test
    public void validTest(){
        assertNotNull(positioningDataTree);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
        assertEquals("31-12-20 16:20", dateFormat.format(positioningDataTree.getFirstDate()));
        assertEquals("31-12-20 17:20", dateFormat.format(positioningDataTree.getLastDate()));
        assertEquals((float)42.7698, positioningDataTree.departureCoordinates().getLatitude());
        assertEquals((float)-66.9759, positioningDataTree.departureCoordinates().getLongitude());
        assertEquals((float)42.97875, positioningDataTree.arrivalCoordinates().getLatitude());
        assertEquals((float)-66.97001, positioningDataTree.arrivalCoordinates().getLongitude());
        assertEquals((float) 60.0, positioningDataTree.totalMovementTime());
        assertEquals((float) 3, positioningDataTree.totalMovementNumber());
        assertEquals((float) 13.3, positioningDataTree.maxSog());
        assertEquals((float) 12.900001, positioningDataTree.meanSog());
        assertEquals((float) 13.1, positioningDataTree.maxCog());
        assertEquals((float) 6.4, positioningDataTree.meanCog());
        assertEquals(23.238515853881836, positioningDataTree.traveledDistance());
        assertEquals(23.238023488476994, positioningDataTree.deltaDistance());
    }

    @Test
    public void invalidTraveledDistance(){
        Coordinate c = new Coordinate((float)-66.97001,((float)42.97875));
        PositioningData p1 = new PositioningData("31/12/2020 17:20", c, (float)12.9, (float)13.1, 355, "Sea", "B");
        PositioningDataTree pl1 = new PositioningDataTree();
        pl1.insertPositioningDataTree(p1);
        assertEquals(0,pl1.traveledDistance());
    }
}