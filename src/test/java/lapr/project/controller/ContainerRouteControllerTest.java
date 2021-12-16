package lapr.project.controller;

import lapr.project.data.ContainerSqlStore;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ContainerRouteControllerTest {
    ContainerRouteController controller;
    MainStorage storage;
    ContainerSqlStore containerStore;

    DatabaseConnection dbconnection;

    @BeforeEach
    void setUp() {
        storage = mock(MainStorage.class);
        containerStore = mock(ContainerSqlStore.class);
        controller = new ContainerRouteController(storage, containerStore);

        dbconnection = mock(DatabaseConnection.class);
    }

    @Test
    void getContainerRoute() {
        //data setup
        int containerNum = 1;
        int shipmentId = 5;
        String clientId = "11111";

        List<List<String>> expected = new ArrayList<>();
        expected.add(Arrays.asList("Location", "Operation", "Mean of Transport", "Timestamp"));
        expected.add(Arrays.asList("Porto", "Loaded", "Ship", "2021-12-01 10:00:00"));

        //database class mocking setup
        when(storage.getDatabaseConnection()).thenReturn(dbconnection);
        when(containerStore.getContainerShipment(dbconnection, clientId, containerNum)).thenReturn(shipmentId);
        when(containerStore.getContainerRoute(dbconnection, shipmentId, containerNum)).thenReturn(expected);

        //method call
        List<List<String>> actual = controller.getContainerRoute(clientId, containerNum);

        //assertion
        assertEquals(actual.size(), expected.size());
        for (int i = 0; i < actual.size(); i++)
            assertEquals(Arrays.deepToString(actual.get(i).toArray()), Arrays.deepToString(expected.get(i).toArray()));
    }

    @Test
    void getContainerRouteToString() {
        List<List<String>> result = new ArrayList<>();
        result.add(Arrays.asList("Location", "Operation", "Mean of Transport", "Timestamp"));
        result.add(Arrays.asList("Porto", "Loaded", "Ship", "2021-12-01 10:00:00"));

        String actual = controller.getContainerRouteToString(result);

        StringBuilder expected = new StringBuilder();
        expected.append("Location  Operation  Mean of Transport  Timestamp  \n");
        expected.append("Porto  Loaded  Ship  2021-12-01 10:00:00  \n");

        assertEquals(actual, expected.toString());
    }
}