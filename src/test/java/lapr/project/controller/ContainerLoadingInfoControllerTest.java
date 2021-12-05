package lapr.project.controller;

import lapr.project.data.ContainerSqlStore;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import lapr.project.data.ShipSqlStore;
import lapr.project.model.Ship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ContainerLoadingInfoControllerTest {
    ContainerLoadingInfoController controller;
    MainStorage storage;
    ContainerSqlStore containerStore;
    ShipSqlStore shipStore;

    DatabaseConnection dbconnection;

    //Data
    Ship ship = new Ship("210950000", "Example", 9450648, "C4SQ2",0,0,0,0);

    @BeforeEach
    void setUp() {
        storage = mock(MainStorage.class);
        containerStore = mock(ContainerSqlStore.class);
        shipStore = mock(ShipSqlStore.class);
        controller = new ContainerLoadingInfoController(storage, containerStore, shipStore);

        dbconnection = mock(DatabaseConnection.class);
    }

    @Test
    void getNextContainerManifest() {
        //data setup
        String captainId = "TestCaptain";
        int portId = 1;
        boolean loading = false;

        List<List<String>> expected = new ArrayList<>();
        expected.add(Arrays.asList("Container no.", "Type", "Position(x)", "Position(y)", "Position(z)", "Load"));
        expected.add(Arrays.asList("1", "refrigerated", "1" ,"1" ,"1", "500"));
        expected.add(Arrays.asList("2", "non-refrigerated", "0" ,"1" ,"1", "500"));

        //database class mocking setup
        when(storage.getDatabaseConnection()).thenReturn(dbconnection);
        when(shipStore.getShipByCaptainId(dbconnection, "TestCaptain")).thenReturn(ship);
        when(containerStore.getContainerManifest(dbconnection, ship.getMmsi(), portId, loading)).thenReturn(expected);

        //method call
        List<List<String>> actual = controller.getNextContainerManifest(captainId, portId, loading);

        //assertion
        assertEquals(actual.size(), expected.size());
        for (int i = 0; i < actual.size(); i++)
            assertEquals(Arrays.deepToString(actual.get(i).toArray()), Arrays.deepToString(expected.get(i).toArray()));
    }

    @Test
    void getNextContainerManifestToString() {
        List<List<String>> result = new ArrayList<>();
        result.add(Arrays.asList("Container no.", "Type", "Position(x)", "Position(y)", "Position(z)", "Load"));
        result.add(Arrays.asList("1", "refrigerated", "1" ,"1" ,"1", "500"));
        result.add(Arrays.asList("2", "non-refrigerated", "0" ,"1" ,"1", "500"));

        String actual = controller.getNextContainerManifestToString(result);

        StringBuilder expected = new StringBuilder();
        expected.append("Container no.  Type  Position(x)  Position(y)  Position(z)  Load  \n");
        expected.append("1  refrigerated  1  1  1  500  \n");
        expected.append("2  non-refrigerated  0  1  1  500  \n");

        assertEquals(actual, expected.toString());
    }

    @Test
    void getNextContainerManifestToStringInvalid() {
        assertEquals(controller.getNextContainerManifestToString(new ArrayList<>()), "No containers found.");
        assertEquals(controller.getNextContainerManifestToString(null), "No containers found.");
    }
}