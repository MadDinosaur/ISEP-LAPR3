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

class GetPositionByDateControllerTest {

    @Test
    public void controllerTest(){

        //Set Up
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

        //End Setup

        GetPositionByDateController controller = new GetPositionByDateController();

        assertNull(controller.getShip());
        assertTrue(controller.setShipByIMO("1000000"));
        assertNotNull(controller.getShip());
        assertFalse(controller.setShipByIMO("1000001"));
        assertNull(controller.getShip());
        assertTrue(controller.setShipByMMSI("111111111"));
        assertNotNull(controller.getShip());
        assertFalse(controller.setShipByMMSI("111111112"));
        assertNull(controller.getShip());
        assertTrue(controller.setShipByCallSign("CS111"));
        assertNotNull(controller.getShip());
        assertFalse(controller.setShipByCallSign( "CS110"));
        assertNull(controller.getShip());

        String bdt1 = "31/12/2000 23:20";
        String bdt2 = "31/12/2000 23:50";
        assertNull(controller.getPositioningByDate(bdt1, bdt2));

        controller.setShipByCallSign("CS111");
        assertEquals(controller.getPositioningByDate(bdt1, bdt2).size(), 2);

        bdt1 = "n";
        bdt2 = "a";
        assertNull(controller.getPositioningByDate(bdt1, bdt2));
    }

}