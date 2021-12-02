package lapr.project.controller;

import oracle.ucp.util.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetOccupancyRateControllerTest {

    @Test
    public void test(){
        GetOccupancyRateController controller = mock(GetOccupancyRateController.class);

        when(controller.getOccupancyRate(100000001,1)).thenReturn(0.56);
        double result = controller.getOccupancyRate(100000001,1);
        assertEquals(0.56, result);
    }
}