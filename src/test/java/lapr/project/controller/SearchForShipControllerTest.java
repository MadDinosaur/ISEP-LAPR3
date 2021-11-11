package lapr.project.controller;

import lapr.project.data.MainStorage;
import lapr.project.model.Coordinate;
import lapr.project.model.PositioningData;
import lapr.project.model.Ship;
import lapr.project.store.ShipStore;
import lapr.project.store.list.PositioningDataList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SearchForShipControllerTest {

    @BeforeEach
    private void setUp(){
        ShipStore shipStore = MainStorage.getInstance().getShipStore();
        PositioningDataList pList1 = new PositioningDataList();
        ArrayList<PositioningData> pDataList1 = new ArrayList<>();



        String shipName = "Example";
        String mmsi = "111111111";

        int imo = 1000000;

        String callSign = "CS111";

        int vesselType = 0;
        float length = 0;
        float width = 0;
        float draft = 0;

        Coordinate c1 = new Coordinate(42.97875f, (float) -66.97001);
        Coordinate c2 = new Coordinate(43.22513f, (float) -66.96725);

        Ship s1 = new Ship(mmsi,shipName,imo,callSign,vesselType,length,width,draft);
        String bdt12 = "31/12/2000 23:38";
        String bdt22 = "31/12/2000 23:49";
        float sog = 0;
        float cog = 359;
        float heading = 0;
        String position = "95";
        String transceiverClass = "A";

        PositioningData d1 = new PositioningData(bdt12,c1,sog,cog,heading,position,transceiverClass);
        PositioningData d2 = new PositioningData(bdt22,c2,sog,cog,heading,position,transceiverClass);

        pDataList1.add(d1);
        pDataList1.add(d2);

        for (PositioningData positioningData : pDataList1)
            pList1.insert(positioningData);

        s1.setPositioningDataList(pList1);

        shipStore.addShip(s1);
    }

    @Test
    public void controllerTest(){
        SearchForShipController controller = new SearchForShipController();
        assertNull(controller.getShipByCallSign("asdas"));
        assertNull(controller.getShipByIMO("asdas"));
        assertNull(controller.getShipByMMSI("asdas"));

        assertNotNull(controller.getShipByCallSign("CS111"));
        assertNotNull(controller.getShipByIMO("1000000"));
        assertNotNull(controller.getShipByMMSI("111111111"));
    }

}