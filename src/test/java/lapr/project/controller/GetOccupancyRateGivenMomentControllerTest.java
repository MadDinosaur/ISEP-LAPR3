package lapr.project.controller;

import lapr.project.data.CargoManifestSqlStore;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import oracle.ucp.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetOccupancyRateGivenMomentControllerTest {
    GetOccupancyRateGivenMomentController controller;
    MainStorage mainStorage;
    DatabaseConnection databaseConnection;
    Connection connection;
    CargoManifestSqlStore cargoManifestStore;

    @BeforeEach
    void setUp() {
        mainStorage =mock(MainStorage.class);
        cargoManifestStore = mock(CargoManifestSqlStore.class);

        controller = new GetOccupancyRateGivenMomentController(mainStorage, cargoManifestStore);

        databaseConnection = mock(DatabaseConnection.class);
        connection = mock(Connection.class);
    }

    @Test
    void getOccupancyRateGivenMoment() {
        //data setup
        int shipMmsi = 100000001;
        String moment = "20/01/2021";

        Pair<String, Double> expected = new Pair("20/01/2021", 20.0);

        //mock classes setup
        when(mainStorage.getDatabaseConnection()).thenReturn(databaseConnection);
        try {when(cargoManifestStore.getOccupancyRateGivenMoment(databaseConnection, shipMmsi, moment)).thenReturn(expected);
        } catch (SQLException throwables) {throwables.printStackTrace();}

        //method call
        Pair<String, Double> actual = controller.getOccupancyRateGivenMoment(shipMmsi, moment);

        assertEquals(actual.get1st(), expected.get1st());
        assertEquals(actual.get2nd(), expected.get2nd());
    }
}