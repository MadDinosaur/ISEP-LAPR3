package lapr.project.controller;

import lapr.project.data.CargoManifestSqlStore;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import oracle.ucp.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetOccupancyRateControllerTest {
    GetOccupancyRateController controller;
    MainStorage mainStorage;
    DatabaseConnection databaseConnection;
    Connection connection;
    CargoManifestSqlStore cargoManifestStore;

    @BeforeEach
    void setUp() {
        mainStorage =mock(MainStorage.class);
        cargoManifestStore = mock(CargoManifestSqlStore.class);

        controller = new GetOccupancyRateController(mainStorage, cargoManifestStore);

        databaseConnection = mock(DatabaseConnection.class);
        connection = mock(Connection.class);
    }

    @Test
    void getOccupancyRate() {
        //data setup
        int shipMmsi = 100000001;
        int manifestId = 1;

        double expected = 20;

        //mock classes setup
        when(mainStorage.getDatabaseConnection()).thenReturn(databaseConnection);
        try {when(cargoManifestStore.getOccupancyRate(databaseConnection, shipMmsi, manifestId)).thenReturn(expected);
        } catch (SQLException throwables) {throwables.printStackTrace();}

        //method call
        double actual = controller.getOccupancyRate(shipMmsi, manifestId);

        assertEquals(actual, expected);
    }
}