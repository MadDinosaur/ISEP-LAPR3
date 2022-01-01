package lapr.project.controller;

import lapr.project.data.ContainerSqlStore;
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

class GetOccupancyMapControllerTest {
    GetOccupancyMapController occupancyMapController;
    MainStorage mainStorage;
    DatabaseConnection databaseConnection;
    Connection connection;
    StorageSqlStore storageSqlStore;

    @BeforeEach
    void setUp() {
        mainStorage =mock(MainStorage.class);
        storageSqlStore = mock(StorageSqlStore.class);

        occupancyMapController = new GetOccupancyMapController(mainStorage, storageSqlStore);

        databaseConnection = mock(DatabaseConnection.class);
        connection = mock(Connection.class);
    }

    @Test
    void getOccupancyMap() {
        //data setup
        int storageId = 3;
        int month = 12;
        int year = 2021;

        List<String> expected = Arrays.asList("21.12.01 00:00:00,000000 | 0,14%", "21.12.04 07:59:23,000000 | 4,71%");

        //mock classes setup
        when(mainStorage.getDatabaseConnection()).thenReturn(databaseConnection);
        when(storageSqlStore.getOccupancyMap(databaseConnection, storageId, month, year)).thenReturn(expected);

        List<String> actual = occupancyMapController.getOccupancyMap(storageId, month, year);

        Assertions.assertEquals(actual.size(), expected.size());
        for (int i = 0; i < actual.size(); i++)
            Assertions.assertEquals(actual.get(i), expected.get(i));
    }

    @Test
    void getOccupancyMapToStringEmpty() {
        String actual1 = occupancyMapController.getOccupancyMapToString(new ArrayList<>());
        String actual2 = occupancyMapController.getOccupancyMapToString(null);
        String expected = "Storage not found.";

        Assertions.assertEquals(expected, actual1);
        Assertions.assertEquals(expected, actual2);
    }

    @Test
    void getOccupancyMapToStringSuccess() {
        String actual = occupancyMapController.getOccupancyMapToString(Arrays.asList("21.12.01 00:00:00,000000 | 0,14%", "21.12.04 07:59:23,000000 | 4,71%"));
        String expected = "21.12.01 00:00:00,000000 | 0,14%\n" +
                        "21.12.04 07:59:23,000000 | 4,71%\n";

        Assertions.assertEquals(expected, actual);
    }
}