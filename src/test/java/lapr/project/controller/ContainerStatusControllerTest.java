package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ContainerStatusControllerTest {
    ContainerStatusController containerStatusController;
    DatabaseConnection databaseConnection;
    Connection connection;

    @BeforeEach
    void setUp() {
        containerStatusController = new ContainerStatusController(mock(MainStorage.class));

        databaseConnection = mock(DatabaseConnection.class);

        connection = mock(Connection.class);

        try {
            connection.setAutoCommit(false);
        } catch (Exception ex) {
            Logger.getLogger(ContainerStatusControllerTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

        when(databaseConnection.getConnection()).thenReturn(connection);
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
}