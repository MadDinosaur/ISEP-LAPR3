package lapr.project.controller;

import oracle.ucp.util.Pair;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetOccupancyRateGivenMomentControllerTest {

    @Test
    public void test() throws ParseException {
        GetOccupancyRateGivenMomentController controller = mock(GetOccupancyRateGivenMomentController.class);

        when(controller.getOccupancyRateGivenMoment(100000001,"2020-09-08 15:45:21")).thenReturn(new Pair<>("String",0.70));

        Pair<String, Double> result = controller.getOccupancyRateGivenMoment(100000001,"2020-09-08 15:45:21");

        assertEquals(0.70, result.get2nd());
    }
}