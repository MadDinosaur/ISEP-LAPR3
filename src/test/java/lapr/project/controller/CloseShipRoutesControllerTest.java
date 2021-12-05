package lapr.project.controller;

import lapr.project.data.MainStorage;
import lapr.project.mappers.dto.ShipDTO;
import lapr.project.model.Coordinate;
import lapr.project.model.PositioningData;
import lapr.project.model.Ship;
import lapr.project.store.ShipStore;
import lapr.project.store.list.PositioningDataTree;
import lapr.project.utils.SorterTraveledDistance;
import oracle.ucp.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class CloseShipRoutesControllerTest {
    CloseShipRoutesController controller = new CloseShipRoutesController();

    PositioningDataTree ship1Position = new PositioningDataTree();
    PositioningDataTree ship2Position = new PositioningDataTree();
    PositioningDataTree ship3Position = new PositioningDataTree();

    ShipStore shipStore = MainStorage.getInstance().getShipStore();

    Coordinate coordinate1, coordinate2, coordinate3, coordinate4, coordinate5, coordinate6;
    PositioningData ship1departure, ship1arrival, ship2departure, ship2arrival, ship3arrival, ship3departure;

    Ship s1, s2, s3;

    @BeforeEach
    public void setUp(){

        coordinate1 = new Coordinate(0, 0);
        coordinate2 = new Coordinate(10, 0);
        coordinate3 = new Coordinate(0.01f, 0);
        coordinate4 = new Coordinate(10.1f, 0);
        coordinate5 = new Coordinate(0.02f, 0);
        coordinate6 = new Coordinate(10.02f, 0);


        ship1departure = new PositioningData("31/12/2020 17:03", coordinate2, 12.5f, 2.4f, 358, "Sea","B");
        ship1arrival = new PositioningData("31/12/2020 17:20", coordinate1, 12.9f, 13.1f, 355, "Sea", "B");
        ship2departure = new PositioningData("31/12/2020 16:20", coordinate4, 13.3f, 3.7f,356, "Sea","B");
        ship2arrival = new PositioningData("31/12/2020 16:30", coordinate5, 15.3f, 2.8f,356, "Sea","B");
        ship3departure = new PositioningData("31/12/2020 16:32", coordinate6, 14.3f, 2.7f,356, "Sea","B");
        ship3arrival = new PositioningData("31/12/2020 17:32", coordinate3, 14.3f, 2.7f,356, "Sea","B");

        ship1Position.insertPositioningDataTree(ship1arrival);
        ship1Position.insertPositioningDataTree(ship1departure);

        ship2Position.insertPositioningDataTree(ship2arrival);
        ship2Position.insertPositioningDataTree(ship2departure);

        ship3Position.insertPositioningDataTree(ship3arrival);
        ship3Position.insertPositioningDataTree(ship3departure);

        String shipName = "Example";
        String mmsi1 = "210950000";
        String mmsi2 = "229857000";
        String mmsi3 = "229850001";

        int imo1 = 9450648;
        int imo2 = 9395044;

        String callSign1 = "C4SQ2";
        String callSign2 = "5BBA4";

        int vesselType = 0;
        float length = 0;
        float width = 0;
        float draft = 0;

        s1 = new Ship(mmsi1,shipName,imo1,callSign1,vesselType,length,width,draft);
        s2 = new Ship(mmsi2,shipName,imo2,callSign2,vesselType,length,width,draft);
        s3 = new Ship(mmsi3,shipName,imo1,callSign1,vesselType,length,width,draft);

        s1.setPositioningDataList(ship1Position);
        s2.setPositioningDataList(ship2Position);
        s3.setPositioningDataList(ship3Position);

        shipStore.addShip(s1);
        shipStore.addShip(s2);
        shipStore.addShip(s3);
    }

    @Test
    void getCloseShipRoutesDTO() {
        List<Pair<Pair<ShipDTO, ShipDTO>,Pair<Double, Double>>> actual = controller.getCloseShipRoutesDTO();

        List<Pair<Pair<String, String>,Pair<Double, Double>>> expected = new ArrayList<>();
        if (!actual.isEmpty())
            expected.add(new Pair(new Pair("229850001", "210950000"), new Pair(2.22,1.11)));

        assertEquals(actual.size(), expected.size());
        for(int i = 0; i < actual.size(); i++) {
            //Compare Ships
            assertEquals(actual.get(i).get1st().get1st().getMmsi(), expected.get(i).get1st().get1st());
            assertEquals(actual.get(i).get1st().get2nd().getMmsi(), expected.get(i).get1st().get2nd());
            //Compare Distances
            assertEquals(String.format("%.2f",actual.get(i).get2nd().get1st()).replace(",","."),
                    expected.get(i).get2nd().get1st().toString());
            assertEquals(String.format("%.2f",actual.get(i).get2nd().get2nd()).replace(",","."),
                    expected.get(i).get2nd().get2nd().toString());
        }
    }

    @Test
    void getCloseShipRoutes() {
        String expected = controller.getCloseShipRoutes().isEmpty() ?
                "No ships available." :
                "Ship 1 MMSI : 229850001 - Ship 2 MMSI : 210950000 - OriginDist : 2,223949 - DestDist : 1,111949 - Traveled Distance1: 1113.061279296875 KM - Number of Movements1: 2.0 - Traveled Distance2: N/A KM - Number of Movements2: N/A\n\n\n";
        String actual = controller.getCloseShipRoutes();

       assertEquals(actual, expected);
    }
}