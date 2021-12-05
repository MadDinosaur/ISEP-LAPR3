package lapr.project.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContainerLoadingInfoControllerTest {
    ContainerLoadingInfoController controller = new ContainerLoadingInfoController();

    @BeforeEach
    void setUp() {
    }

    @Test
    void getNextContainerManifest() {
    }

    @Test
    void getNextContainerManifestToString() {
        List<List<String>> result = new ArrayList<>();
        result.add(Arrays.asList("Container no.", "Type", "Position(x)", "Position(y)", "Position(z)", "Load"));
        result.add(Arrays.asList("1", "refrigerated", "1" ,"1" ,"1", "500"));
        result.add(Arrays.asList("2", "non-refrigerated", "0" ,"1" ,"1", "500"));

        String actual = controller.getNextContainerManifestToString(result);

        StringBuilder expected = new StringBuilder();
        expected.append("Container no.  Type  Position(x)  Position(y)  Position(z)  Load  \n");
        expected.append("1  refrigerated  1  1  1  500  \n");
        expected.append("2  non-refrigerated  0  1  1  500  \n");

        assertEquals(actual, expected.toString());
    }

    @Test
    void getNextContainerManifestToStringInvalid() {
        assertEquals(controller.getNextContainerManifestToString(new ArrayList<>()), "No containers found.");
        assertEquals(controller.getNextContainerManifestToString(null), "No containers found.");
    }
}