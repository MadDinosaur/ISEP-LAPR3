package lapr.project.store.list;

import lapr.project.model.Coordinate;
import lapr.project.model.PositioningData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        List<PositioningData> positioning = new ArrayList<>();
        coordinate = new Coordinate((float)-66.97001,((float)42.97875));
        positioningData = new PositioningData("31/12/2020 17:20", coordinate, (float)12.9, (float)13.1, 355, "Sea", "B");
        positioning.add(positioningData);
        coordinate2 = new Coordinate((float)-66.97243, (float)42.92236);
        positioningData2 = new PositioningData("31/12/2020 17:03", coordinate2, (float)12.5, (float)2.4, 358, "Sea","B");
        positioning.add(positioningData2);
        coordinate3 = new Coordinate((float) -66.9759, (float)42.7698);
        positioningData3 = new PositioningData("31/12/2020 16:20", coordinate3, (float)13.3, (float)3.7,356, "Sea","B");
        positioning.add(positioningData3);
        positioningDataList.addPositioningDataList(positioning);
    }

    @Test
    public void validTest(){
        assertNotNull(positioningDataList);
        assertEquals("Thu Dec 31 16:20:00 WET 2020", positioningDataList.getFirstDate().toString());
        assertEquals("Thu Dec 31 17:20:00 WET 2020", positioningDataList.getLastDate().toString());
        assertEquals((float)42.7698,positioningDataList.departureLatitude());
        assertEquals((float)-66.9759, positioningDataList.departureLongitude());
        assertEquals((float)42.97875, positioningDataList.arrivalLatitude());
        assertEquals((float)-66.97001,positioningDataList.arrivalLongitude());
        assertEquals((float) 3600000.0, positioningDataList.totalMovementTime());
        assertEquals((float) 3, positioningDataList.totalMovementNumber());
        assertEquals((float) 13.3, positioningDataList.maxSog());
        assertEquals((float) 12.899999, positioningDataList.meanSog());
        assertEquals((float) 13.1, positioningDataList.maxCog());
        assertEquals((float) 6.4, positioningDataList.meanCog());

    }
}