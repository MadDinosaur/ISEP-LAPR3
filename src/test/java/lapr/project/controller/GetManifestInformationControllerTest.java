package lapr.project.controller;

import oracle.ucp.util.Pair;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetManifestInformationControllerTest {

    @Test
    public void test(){
        GetManifestInformationController controller = new GetManifestInformationController();
        controller = mock(GetManifestInformationController.class);

        when(controller.findCargoManifests(1, 2020)).thenReturn(new Pair<>(2, 2));
        Pair<Integer, Integer> result = controller.findCargoManifests(1, 2020);
        assertEquals(new Pair<>(2, 2), result);
    }

}