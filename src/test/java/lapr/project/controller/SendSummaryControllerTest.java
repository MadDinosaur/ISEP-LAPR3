package lapr.project.controller;

import lapr.project.data.MainStorage;
import lapr.project.model.Coordinate;
import lapr.project.model.PositioningData;
import lapr.project.model.Ship;
import lapr.project.store.ShipStore;
import lapr.project.store.list.PositioningDataTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SendSummaryControllerTest {

    @BeforeEach
    public void setUp(){
        ShipStore shipStore = MainStorage.getInstance().getShipStore();
        PositioningDataTree pList1 = new PositioningDataTree();
        ArrayList<PositioningData> pDataList1 = new ArrayList<>();



        String shipName = "Ship";
        String mmsi = "210950000";
        int imo = 1000000;
        String callSign = "CS111";
        int vesselType = 70;
        float length = 166;
        float width = 25;
        float draft = (float) 9.5;
        Coordinate c1 = new Coordinate((float) -66.97001, 42.97875f);
        Coordinate c2 = new Coordinate((float) -66.97243,42.92236f);
        Coordinate c3 = new Coordinate((float) -66.9759,42.7698f);
        Ship s1 = new Ship(mmsi,shipName,imo,callSign,vesselType,length,width,draft);
        String bdt12 = "31/12/2000 17:20";
        String bdt22 = "31/12/2000 17:03";
        String bdt32 = "31/12/2000 16:20";
        float sog = (float) 12.9;
        float cog = (float)13.1;
        float heading = 355;
        String position = "95";
        String transceiverClass = "A";

        PositioningData d1 = new PositioningData(bdt12,c1,sog,cog,heading,position,transceiverClass);
        PositioningData d2 = new PositioningData(bdt22,c2,(float) 12.5,(float) 2.4,heading,position,transceiverClass);
        PositioningData d3 = new PositioningData(bdt32,c3,(float) 13.3,(float) 3.7,heading,position,transceiverClass);

        pDataList1.add(d1);
        pDataList1.add(d2);
        pDataList1.add(d3);

        for (PositioningData positioningData : pDataList1)
            pList1.insert(positioningData);

        s1.setPositioningDataList(pList1);

        shipStore.addShip(s1);
    }

    @Test
    public void testController(){
        SendSummaryController controller = new SendSummaryController();
        assertNotNull(controller.getShipByCodeType("210950000"));
        assertNull(controller.getShipByCodeType("201093080"));
        assertNotNull(controller.getShipByCodeType("1000000"));
        assertNull(controller.getShipByCodeType("1000001"));
        assertNotNull(controller.getShipByCodeType("CS111"));
        assertNull(controller.getShipByCodeType("toNull"));
        assertNull(controller.getShipByCodeType(null));
    }

    @Test
    public void testToSummary(){
        SendSummaryController controller = new SendSummaryController();
        Ship s = controller.getShipByCodeType("210950000");
        assertNotNull(controller.toSummary("210950000"));
        assertNull(controller.toSummary(null));
        assertNull(controller.toSummary("toNull"));
    }
}