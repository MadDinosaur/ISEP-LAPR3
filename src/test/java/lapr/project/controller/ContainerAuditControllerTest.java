package lapr.project.controller;

import lapr.project.data.ContainerSqlStore;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import lapr.project.data.ShipSqlStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ContainerAuditControllerTest {
    ContainerAuditController controller;
    MainStorage storage;
    ContainerSqlStore containerStore;

    DatabaseConnection dbconnection;

    @BeforeEach
    void setUp() {
        storage = mock(MainStorage.class);
        containerStore = mock(ContainerSqlStore.class);
        controller = new ContainerAuditController(storage, containerStore);

        dbconnection = mock(DatabaseConnection.class);
    }

    @Test
    void getContainerAudit() {
        //data setup
        int containerNum = 1;
        int cargoManifestId = 1;

        List<List<String>> expected = new ArrayList<>();
        expected.add(Arrays.asList("user_id", "date_time", "operation_type", "container_num", "cargo_manifest_id"));
        expected.add(Arrays.asList("MYUSER", "2021-12-12 23:07:10", "INSERT" ,"1" ,"1"));

        //database class mocking setup
        when(storage.getDatabaseConnection()).thenReturn(dbconnection);
        when(containerStore.getContainerAudit(dbconnection, containerNum, cargoManifestId)).thenReturn(expected);

        //method call
        List<List<String>> actual = controller.getContainerAudit(containerNum, cargoManifestId);

        //assertion
        assertEquals(actual.size(), expected.size());
        for (int i = 0; i < actual.size(); i++)
            assertEquals(Arrays.deepToString(actual.get(i).toArray()), Arrays.deepToString(expected.get(i).toArray()));
    }

    @Test
    void getContainerAuditToString() {
        List<List<String>> result = new ArrayList<>();
        result.add(Arrays.asList("user_id", "date_time", "operation_type", "container_num", "cargo_manifest_id"));
        result.add(Arrays.asList("MYUSER", "2021-12-12 23:07:10", "INSERT" ,"1" ,"1"));

        String actual = controller.getContainerAuditToString(result);

        StringBuilder expected = new StringBuilder();
        expected.append("user_id  date_time  operation_type  container_num  cargo_manifest_id  \n");
        expected.append("MYUSER  2021-12-12 23:07:10  INSERT  1  1  \n");

        assertEquals(actual, expected.toString());
    }
}