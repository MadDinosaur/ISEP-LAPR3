package lapr.project.controller;

import lapr.project.data.MainStorage;
import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.model.*;
import lapr.project.store.ShipStore;
import lapr.project.store.StorageStore;
import lapr.project.store.list.PositioningDataTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NearestPortControllerTest {

    StorageStore storageStore = MainStorage.getInstance().getStorageStore();
    ShipStore shipStore = MainStorage.getInstance().getShipStore();
    Storage storage1, storage2, storage3;
    Coordinate coord1, coord2, coord3, coord4;
    Ship s1;
    List<Tree2D.Node<Storage>> storageList = new ArrayList<>();

    @BeforeEach
    void setUp() {

        coord1 = new Coordinate(14.54306f, 35.84194f);
        coord2 = new Coordinate(10.21666667f, 56.15f);
        coord3 = new Coordinate(12.61666667f, 55.7f);
        coord4 = new Coordinate(9.61666667f, 57.7f);

        storage1 = new Storage(10138, "Marsaxlokk", "Europe", "Malta", coord1);
        storage2 = new Storage(10358, "Aarhus", "Europe", "Denmark", coord2);
        storage3 = new Storage(10563, "Copenhagen", "Europe", "Denmark", coord3);

        storageList.add(new Tree2D.Node<>(storage1, coord1.getLongitude(), coord1.getLatitude()));
        storageList.add(new Tree2D.Node<>(storage2, coord2.getLongitude(), coord2.getLatitude()));
        storageList.add(new Tree2D.Node<>(storage3, coord3.getLongitude(), coord3.getLatitude()));

        PositioningDataTree positioningDataTree = new PositioningDataTree();
        storageStore.insert(new ArrayList<>());

        s1 = new Ship("100000001","ship",1234567,"CSSH",32,10,10,10);
        s1.setPositioningDataList(positioningDataTree);
        for (Ship ship : shipStore.inOrder())
            shipStore.remove(ship);
        shipStore.addShip(s1);
    }

    @Test
    public void NoShipcontrollerTest() {
        NearestPortController controller = new NearestPortController();
        PositioningDataDTO positioningData = controller.getPositioningData("CSSH1", "31/12/2020 17:40");
        assertNull(positioningData);
    }

    @Test
    public void noPositioningDataControllerTest() {
        NearestPortController controller = new NearestPortController();
        PositioningDataDTO positioningData = controller.getPositioningData("CSSH", "31/12/2020 17:40");
        assertNull(positioningData);
    }

    @Test
    public void validControllerTest() {
        NearestPortController controller = new NearestPortController();
        s1.getPositioningDataList().insert( new PositioningData("31/12/2020 17:20", coord4, (float)12.9, (float)13.1, 355, "Sea", "B"));
        PositioningDataDTO positioningData = controller.getPositioningData("CSSH", "31/12/2020 17:40");
        assertEquals(positioningData.getBdt(), "31/12/2020 17:20");
    }

    @Test
    public void noPortControllerTest() {
        NearestPortController controller = new NearestPortController();
        s1.getPositioningDataList().insert( new PositioningData("31/12/2020 17:20", coord4, (float)12.9, (float)13.1, 355, "Sea", "B"));
        PositioningDataDTO positioningData = controller.getPositioningData("CSSH", "31/12/2020 17:40");
        assertEquals(positioningData.getBdt(), "31/12/2020 17:20");

        assertNull(controller.getNearestStorage(Float.parseFloat(positioningData.getLongitude()), Float.parseFloat(positioningData.getLatitude())));
    }


    @Test
    public void validPortControllerTest() {
        NearestPortController controller = new NearestPortController();
        s1.getPositioningDataList().insert( new PositioningData("31/12/2020 17:20", coord4, (float)12.9, (float)13.1, 355, "Sea", "B"));
        PositioningDataDTO positioningData = controller.getPositioningData("CSSH", "31/12/2020 17:40");
        assertEquals(positioningData.getBdt(), "31/12/2020 17:20");

        storageStore.insert(storageList);
        assertEquals(controller.getNearestStorage(Float.parseFloat(positioningData.getLongitude()), Float.parseFloat(positioningData.getLatitude())).getIdentification(), "10358");
    }
}