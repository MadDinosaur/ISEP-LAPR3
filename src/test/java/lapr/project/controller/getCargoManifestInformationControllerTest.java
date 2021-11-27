package lapr.project.controller;

import oracle.ucp.util.Pair;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;

class getCargoManifestInformationControllerTest {


    @Test
    public void DataTest() throws SQLException {
        GetCargoManifestInformationController controller = new GetCargoManifestInformationController();
        Pair<Integer, Integer> resultSet = controller.findCargoManifests(1, 2020);
        assertEquals(6, resultSet.get1st());
        assertEquals(3, resultSet.get2nd());
    }
}