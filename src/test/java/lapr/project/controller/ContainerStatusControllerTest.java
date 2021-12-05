package lapr.project.controller;

import lapr.project.data.ContainerSqlStore;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ContainerStatusControllerTest {
    ContainerStatusController containerStatusController;
    MainStorage mainStorage;
    DatabaseConnection databaseConnection;
    Connection connection;
    ContainerSqlStore containerStore;

    @BeforeEach
    void setUp() {
        mainStorage =mock(MainStorage.class);
        containerStore = mock(ContainerSqlStore.class);

        containerStatusController = new ContainerStatusController(mainStorage, containerStore);

        databaseConnection = mock(DatabaseConnection.class);
        connection = mock(Connection.class);
    }

    @Test
    void getContainerStatusToStringNonExistent() {
        String actual = containerStatusController.getContainerStatusToString(new ArrayList<>());
        String expected = "Container not found.";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getContainerStatusToStringNonInvalidInput() {
        String actual = containerStatusController.getContainerStatusToString(Arrays.asList("Ship", "Titanic"));
        String expected = "Input parameters are incorrect. No containers can be searched.";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getContainerStatusToStringSuccess() {
        String actual = containerStatusController.getContainerStatusToString(Arrays.asList("1", "Ship", "Titanic"));
        String expected = "Container no. 1 is currently in Ship Titanic";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getContainerStatus() {
        //data setup
        int containerNum = 1;

        List<String> expected = Arrays.asList("1", "Ship", "Titanic");

        //mock classes setup
        when(mainStorage.getDatabaseConnection()).thenReturn(databaseConnection);
        when(containerStore.getContainerStatus(databaseConnection, containerNum)).thenReturn(expected);

        List<String> actual = containerStatusController.getContainerStatus(containerNum);

        Assertions.assertEquals(actual.size(), expected.size());
        for (int i = 0; i < actual.size(); i++)
            Assertions.assertEquals(actual.get(i), expected.get(i));
    }
}