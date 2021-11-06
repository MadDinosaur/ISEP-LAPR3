package lapr.project.store;

import lapr.project.model.Ship;
import lapr.project.store.list.PositioningDataList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class ShipTreeTest {
    private static String shipName = "ship";
    private static int vesselType = 12345;
    private static float length = 40;
    private static float width = 40;
    private static float draft = 14;
    private int[] imoOrder = {1111113, 1111114, 1111115, 1111116, 1111117, 1111118, 1111119, 1111120};
    private String[] mmsiOrder = {"111111111","111111112", "111111113", "111111114", "111111115", "111111116", "111111117", "111111118"};
    private String[] CSOrder = {"CS111","CS112", "CS113", "CS115", "CS116", "CS117", "CS118", "CS119"};

    @Test
    public void OrderTest(){
        List<Ship> shipList = new ArrayList<>();
        Ship ship1 = new Ship("111111111", shipName, 1111120, "CS111", vesselType, length, width, draft);
        ship1.setPositioningDataList(new PositioningDataList());
        shipList.add(ship1);
        shipList.add(new Ship("111111112", shipName, 1111119, "CS116", vesselType, length, width, draft));
        shipList.add(new Ship("111111113", shipName, 1111118, "CS119", vesselType, length, width, draft));
        shipList.add(new Ship("111111114", shipName, 1111117, "CS115", vesselType, length, width, draft));
        shipList.add(new Ship("111111115", shipName, 1111116, "CS113", vesselType, length, width, draft));
        shipList.add(new Ship("111111116", shipName, 1111115, "CS112", vesselType, length, width, draft));
        shipList.add(new Ship("111111117", shipName, 1111114, "CS118", vesselType, length, width, draft));
        shipList.add(new Ship("111111118", shipName, 1111113, "CS117", vesselType, length, width, draft));
        shipList.add(ship1);

        ShipTree mmsiSorted = new ShipTree();
        ShipTree imoSorted = new ShipTree();
        ShipTree CallSignSorted = new ShipTree();

        for (Ship ship : shipList){
            mmsiSorted.insertMMSI(ship);
            imoSorted.insertIMO(ship);
            CallSignSorted.insertCallSign(ship);
        }

        List<Ship> mmsiList = (List<Ship>) mmsiSorted.inOrder();
        List<Ship> imoList = (List<Ship>) imoSorted.inOrder();
        List<Ship> callSignList = (List<Ship>) CallSignSorted.inOrder();

        for (int i = 0; i < mmsiList.size(); i++){
            assertEquals(mmsiList.get(i).getMmsi(), mmsiOrder[i]);
            assertEquals(imoList.get(i).getImo(), imoOrder[i]);
            assertEquals(callSignList.get(i).getCallSign(), CSOrder[i]);
        }

        assertEquals(mmsiSorted.findMMSI("111111111"), ship1);
        assertEquals(imoSorted.findIMO(1111120), ship1);
        assertEquals(CallSignSorted.findCallSign("CS111"), ship1);
    }
}