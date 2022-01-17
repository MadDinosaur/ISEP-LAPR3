package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import lapr.project.data.StorageSqlStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetLoadingUnloadingMapControllerTest {
    GetLoadingUnloadingMapController loadingUnloadingMapController;
    MainStorage mainStorage;
    DatabaseConnection databaseConnection;
    Connection connection;
    StorageSqlStore storageSqlStore;

    @BeforeEach
    void setUp() {
        mainStorage =mock(MainStorage.class);
        storageSqlStore = mock(StorageSqlStore.class);

        loadingUnloadingMapController = new GetLoadingUnloadingMapController(mainStorage, storageSqlStore);

        databaseConnection = mock(DatabaseConnection.class);
        connection = mock(Connection.class);
    }
    @Test
    void getLoadingUnloadingMap() {
        //data setup
        String portManagerId = "9";
        String date = "2020-05-19 00:00:00";

        List<String> expected = Arrays.asList("1\tLoad\t21.11.26 20:45:24,000000\tShip\t100000001\t1\t2\t(1,2,1)",
                "1\tLoad\t21.12.01 07:59:23,000000\tShip\t100000001\t1\t3\t(0,0,0)");

        //mock classes setup
        when(mainStorage.getDatabaseConnection()).thenReturn(databaseConnection);
        when(storageSqlStore.getLoadingUnloadingMap(databaseConnection, portManagerId, date)).thenReturn(expected);

        List<String> actual = loadingUnloadingMapController.getLoadingUnloadingMap(portManagerId, date);

        Assertions.assertEquals(actual.size(), expected.size());
        for (int i = 0; i < actual.size(); i++)
            Assertions.assertEquals(actual.get(i), expected.get(i));
    }

    @Test
    void getLoadingUnloadingMapToStringEmpty() {
        String actual1 = loadingUnloadingMapController.getLoadingUnloadingMapToString(new ArrayList<>());
        String actual2 = loadingUnloadingMapController.getLoadingUnloadingMapToString(null);
        String expected = "Unable to generate map.";

        Assertions.assertEquals(expected, actual1);
        Assertions.assertEquals(expected, actual2);
    }

    @Test
    void getLoadingUnloadingMapToStringSuccess() {
        String actual = loadingUnloadingMapController.getLoadingUnloadingMapToString(Arrays.asList("1\tLoad\t21.11.26 20:45:24,000000\tShip\t100000001\t1\t2\t(1,2,1)",
                "1\tLoad\t21.12.01 07:59:23,000000\tShip\t100000001\t1\t3\t(0,0,0)"));
        String expected = "Port ID | Operation Type | Load/Unload Date | Vehicle | ID | No. of Container to Load/Unload | Container No. | Container Position\n" +
                "1\tLoad\t21.11.26 20:45:24,000000\tShip\t100000001\t1\t2\t(1,2,1)\n" +
                "1\tLoad\t21.12.01 07:59:23,000000\tShip\t100000001\t1\t3\t(0,0,0)\n";

        Assertions.assertEquals(expected, actual);
    }
}
