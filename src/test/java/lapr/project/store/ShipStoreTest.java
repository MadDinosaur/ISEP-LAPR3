package lapr.project.store;

import lapr.project.model.BST;
import lapr.project.model.Coordinate;
import lapr.project.model.PositioningData;
import lapr.project.model.Ship;
import lapr.project.store.list.PositioningDataList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ShipStoreTest {

    @BeforeEach
    public void setUp(){

        ShipStore shipStore = new ShipStore();
        PositioningDataList pList1 = new PositioningDataList();
        PositioningDataList pList2 = new PositioningDataList();
        ArrayList<PositioningData> pDataList1 = new ArrayList<>();
        ArrayList<PositioningData> pDataList2 = new ArrayList<>();



        String shipName = "Example";
        String mmsi1 = "111111111";
        String mmsi2 = "222222222";

        int imo1 = 1000000;
        int imo2 = 2000000;

        String callSign1 = "CS111";
        String callSign2 = "CS222";

        int vesselType = 0;
        float length = 0;
        float width = 0;
        float draft = 0;

        Coordinate c1 = new Coordinate(42.97875f, (float) -66.97001);
        Coordinate c2 = new Coordinate(43.22513f, (float) -66.96725);

        Ship s1 = new Ship(mmsi1,shipName,imo1,callSign1,vesselType,length,width,draft);
        Ship s2 = new Ship(mmsi2,shipName,imo2,callSign2,vesselType,length,width,draft);

        String bdt = "31/12/2000 23:38";
        float sog = 0;
        float cog = 359;
        float heading = 0;
        String position = "95";
        String transceiverClass = "A";

        PositioningData d1 = new PositioningData(bdt,c1,sog,cog,heading,position,transceiverClass);
        PositioningData d2 = new PositioningData(bdt,c2,sog,cog,heading,position,transceiverClass);

        pDataList1.add(d1);
        pDataList2.add(d2);

        for (PositioningData positioningData : pDataList1)
            pList1.insert(positioningData);
        for (PositioningData positioningData : pDataList2)
            pList2.insert(positioningData);

        s1.setPositioningDataList(pList1);
        s2.setPositioningDataList(pList2);



        shipStore.addShip(s1);
        shipStore.addShip(s2);

    }


    @Test
    public void validateTest(){
        ShipStore shipStore = new ShipStore();
        assertFalse(shipStore.addShip(null));
        assertNull(shipStore.getShipByIMO("a"));
        assertNull(shipStore.getShipByCallSign("a"));
    }

//    @Test
//    public void sortShipsTestNotNull(){
//        ShipStore shipStore = new ShipStore();
//        assertNotNull(shipStore.sortShips());
//    }

}