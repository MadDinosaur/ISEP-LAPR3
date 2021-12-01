package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
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
        String actual = containerStatusController.getContainerStatusToString(-1);
        String expected = "Container not found.";

        Assertions.assertEquals(expected, actual);
    }
}