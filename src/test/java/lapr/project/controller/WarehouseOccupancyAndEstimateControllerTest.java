package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import lapr.project.data.StorageSqlStore;
import oracle.ucp.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WarehouseOccupancyAndEstimateControllerTest {
    WarehouseOccupancyAndEstimateController controller;
    MainStorage mainStorage;
    DatabaseConnection databaseConnection;
    Connection connection;
    StorageSqlStore storageStore;

    @BeforeEach
    void setUp() {
        mainStorage =mock(MainStorage.class);
        storageStore = mock(StorageSqlStore.class);

        controller = new WarehouseOccupancyAndEstimateController(mainStorage, storageStore);

        databaseConnection = mock(DatabaseConnection.class);
        connection = mock(Connection.class);
    }


    @Test
    public void testGetOccupancyRate(){
        int storageId = 1;

        Pair<Integer, Double> expected = new Pair(1, 0.064);

        when(mainStorage.getDatabaseConnection()).thenReturn(databaseConnection);
        try {when(storageStore.getOccupancyRate(databaseConnection, storageId)).thenReturn(expected);
        } catch (SQLException throwables) {throwables.printStackTrace();}

        Pair<Integer, Double> actual = controller.getOccupancyRate(storageId);

        assertEquals(actual.get1st(), expected.get1st());
        assertEquals(actual.get2nd(), expected.get2nd());
    }

    @Test
    public void testEstimate(){
        int storageId = 2;
        Pair<Integer, Integer> expected = new Pair(2,2);

        when(mainStorage.getDatabaseConnection()).thenReturn(databaseConnection);
        try {when(storageStore.getEstimateLeavingContainers30Days(databaseConnection, storageId)).thenReturn(expected);
        } catch (SQLException throwables) {throwables.printStackTrace();}

        Pair<Integer, Integer> actual = controller.getEstimateLeavingContainers30Days(storageId);

        assertEquals(actual.get1st(), expected.get1st());
        assertEquals(actual.get2nd(), expected.get2nd());
    }
}