package lapr.project.controller;

import lapr.project.data.CargoManifestSqlStore;
import lapr.project.data.ContainerSqlStore;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import oracle.ucp.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetManifestInformationControllerTest {
    GetManifestInformationController controller;
    MainStorage mainStorage;
    DatabaseConnection databaseConnection;
    Connection connection;
    CargoManifestSqlStore cargoManifestStore;

    @BeforeEach
    void setUp() {
        mainStorage =mock(MainStorage.class);
        cargoManifestStore = mock(CargoManifestSqlStore.class);

        controller = new GetManifestInformationController(mainStorage, cargoManifestStore);

        databaseConnection = mock(DatabaseConnection.class);
        connection = mock(Connection.class);
    }

    @Test
    void findCargoManifests() {
        //data setup
        int captainId = 1;
        int year = 2021;

        Pair<Integer, Double> expected = new Pair(2, 3.4);

        //mock classes setup
        when(mainStorage.getDatabaseConnection()).thenReturn(databaseConnection);
        try {when(cargoManifestStore.getCargoManifestInYear(databaseConnection, captainId, year)).thenReturn(expected);
        } catch (SQLException throwables) {throwables.printStackTrace();}

        //method call
        Pair<Integer, Double> actual = controller.findCargoManifests(captainId, year);

        assertEquals(actual.get1st(), expected.get1st());
        assertEquals(actual.get2nd(), expected.get2nd());
    }
}