package lapr.project.data;

import lapr.project.model.Container;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ContainerSqlStoreTest {
    ContainerSqlStore containerSqlStore;
    DatabaseConnection databaseConnection;
    Connection connection;
    ResultSet resultSet;
    ResultSetMetaData resultSetHeaders;
    Container container = new Container(200031, 7, "22G1", 30480.0, 2180.0, 28300.0, 33.1, false);

    @BeforeEach
    void setUp() {
        containerSqlStore = new ContainerSqlStore();

        databaseConnection = mock(DatabaseConnection.class);

        connection = mock(Connection.class);

        resultSet = mock(ResultSet.class);
        resultSetHeaders = mock(ResultSetMetaData.class);

        try {
            connection.setAutoCommit(false);
        } catch (Exception ex) {
            Logger.getLogger(ContainerSqlStoreTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

        when(databaseConnection.getConnection()).thenReturn(connection);
    }

    @Test
    void saveNull() {
        Assertions.assertFalse(containerSqlStore.save(null, null));
    }

    @Test
    void saveNotNull() {
        try {
            String sqlCommand = "insert into container values (?,?,?,?,?,?,?,?)";
            PreparedStatementTest preparedStatementTest = new PreparedStatementTest(sqlCommand);

            when(connection.prepareStatement(sqlCommand)).thenReturn(preparedStatementTest);

            Assertions.assertTrue(containerSqlStore.save(databaseConnection, container));

            String expected = "insert into container values (200031,7,22G1,30480.0,2180.0,28300.0,33.1,0)";
            Assertions.assertEquals(expected, preparedStatementTest.toString());
        } catch (Exception ex) {
            Logger.getLogger(ContainerSqlStoreTest.class.getName())
                    .log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Test
    void deleteNull() {
        Assertions.assertFalse(containerSqlStore.delete(null, null));
    }

    @Test
    void deleteNotNull() {
        try {
            String sqlCommand = "delete from container where num = ?";
            PreparedStatementTest preparedStatementTest = new PreparedStatementTest(sqlCommand);

            when(connection.prepareStatement(sqlCommand)).thenReturn(preparedStatementTest);

            Assertions.assertTrue(containerSqlStore.delete(databaseConnection, container));

            String expected = "delete from container where num = 200031";
            Assertions.assertEquals(expected, preparedStatementTest.toString());
        } catch (Exception ex) {
            Logger.getLogger(ContainerSqlStoreTest.class.getName())
                    .log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Test
    void getContainerStatusNull() {
        Assertions.assertTrue(containerSqlStore.getContainerStatus(null, -1).isEmpty());
    }

    @Test
    void getContainerStatusNotNull() {
        try {
            //Setup statement and mock result
            String sqlCommand = "SELECT * FROM(\n" +
                    "       SELECT CONTAINER.NUM as CONTAINER_NUM,\n" +
                    "           (CASE WHEN CP.LOADING_FLAG = 1 THEN 'Ship' ELSE 'Port' END) as LOCATION_TYPE,\n" +
                    "           (CASE WHEN CP.LOADING_FLAG = 1 THEN (SELECT NAME FROM SHIP WHERE MMSI = CP.SHIP_MMSI) ELSE (SELECT NAME FROM STORAGE WHERE IDENTIFICATION = CP.STORAGE_IDENTIFICATION) END) as LOCATION_NAME\n" +
                    "        FROM CONTAINER\n" +
                    "        INNER JOIN CONTAINER_CARGOMANIFEST ON CONTAINER_CARGOMANIFEST.CONTAINER_NUM = CONTAINER.NUM\n" +
                    "        INNER JOIN CARGOMANIFEST_PARTIAL CP on CONTAINER_CARGOMANIFEST.PARTIAL_CARGO_MANIFEST_ID = CP.ID\n" +
                    "        WHERE CONTAINER.NUM = pContainer_Num AND CP.STATUS LIKE 'finished'\n" +
                    "        ORDER BY CP.FINISHING_DATE_TIME DESC)\n" +
                    "    WHERE ROWNUM = 1";

            PreparedStatementTest preparedStatementTest = new PreparedStatementTest(sqlCommand, resultSet);

            when(connection.prepareStatement(sqlCommand)).thenReturn(preparedStatementTest);
            when(resultSet.next()).thenReturn(true).thenReturn(false);
            when(resultSet.getMetaData()).thenReturn(resultSetHeaders);
            when(resultSetHeaders.getColumnName(1)).thenReturn("container_num");
            when(resultSet.getString(1)).thenReturn("200031");
            when(resultSetHeaders.getColumnName(2)).thenReturn("location_type");
            when(resultSet.getString(2)).thenReturn("ship");
            when(resultSetHeaders.getColumnName(3)).thenReturn("location_name");
            when(resultSet.getString(3)).thenReturn("100000001");
            when(resultSetHeaders.getColumnCount()).thenReturn(3);

            //Method call
            List<String> actual = containerSqlStore.getContainerStatus(databaseConnection, 200031);

            //SQL command build test
            String expectedSqlCommand = "SELECT * FROM(\n" +
                    "       SELECT CONTAINER.NUM as CONTAINER_NUM,\n" +
                    "           (CASE WHEN CP.LOADING_FLAG = 1 THEN 'Ship' ELSE 'Port' END) as LOCATION_TYPE,\n" +
                    "           (CASE WHEN CP.LOADING_FLAG = 1 THEN (SELECT NAME FROM SHIP WHERE MMSI = CP.SHIP_MMSI) ELSE (SELECT NAME FROM STORAGE WHERE IDENTIFICATION = CP.STORAGE_IDENTIFICATION) END) as LOCATION_NAME\n" +
                    "        FROM CONTAINER\n" +
                    "        INNER JOIN CONTAINER_CARGOMANIFEST ON CONTAINER_CARGOMANIFEST.CONTAINER_NUM = CONTAINER.NUM\n" +
                    "        INNER JOIN CARGOMANIFEST_PARTIAL CP on CONTAINER_CARGOMANIFEST.PARTIAL_CARGO_MANIFEST_ID = CP.ID\n" +
                    "        WHERE CONTAINER.NUM = pContainer_Num AND CP.STATUS LIKE 'finished'\n" +
                    "        ORDER BY CP.FINISHING_DATE_TIME DESC)\n" +
                    "    WHERE ROWNUM = 1";
            Assertions.assertEquals(expectedSqlCommand, preparedStatementTest.toString());

            //SQL query result wrapping test
            List<String> expected = Arrays.asList("200031", "ship", "100000001");

            Assertions.assertEquals(actual.size(), 0);
            for (int i = 0; i < actual.size(); i++) {
                Assertions.assertEquals(actual.get(i), expected.get(i));
            }
        } catch (Exception ex) {
            Logger.getLogger(ContainerSqlStoreTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void getContainerManifestNull() {
        Assertions.assertTrue(containerSqlStore.getContainerManifest(null, null, -1, false).isEmpty());
        Assertions.assertTrue(containerSqlStore.getContainerManifest(null, null, -1, true).isEmpty());
    }

    @Test
    void getContainerManifestNotNull() {
        try {
            //Setup statement and mock result
            String sqlCommand = "SELECT NUM,\n" +
                    "        (CASE WHEN REFRIGERATED_FLAG = 1 THEN 'Refrigerated' ELSE 'Non-refrigerated' END) as \"TYPE\",\n" +
                    "        CONTAINER_POSITION_X, CONTAINER_POSITION_Y, CONTAINER_POSITION_Z,\n" +
                    "        PAYLOAD\n" +
                    "        FROM CONTAINER INNER JOIN CONTAINER_CARGOMANIFEST ON CONTAINER.NUM = CONTAINER_CARGOMANIFEST.CONTAINER_NUM\n" +
                    "        WHERE PARTIAL_CARGO_MANIFEST_ID IN\n" +
                    "          (SELECT ID\n" +
                    "          FROM CARGOMANIFEST_PARTIAL\n" +
                    "          WHERE SHIP_MMSI = ?\n" +
                    "            AND STORAGE_IDENTIFICATION = ?\n" +
                    "            AND LOADING_FLAG = ?\n" +
                    "            AND STATUS LIKE 'finished')";

            PreparedStatementTest preparedStatementTest = new PreparedStatementTest(sqlCommand, resultSet);

            when(connection.prepareStatement(sqlCommand)).thenReturn(preparedStatementTest);
            when(resultSet.getMetaData()).thenReturn(resultSetHeaders);
            when(resultSet.next()).thenReturn(true).thenReturn(false);
            when(resultSetHeaders.getColumnName(1)).thenReturn("container_num");
            when(resultSet.getString(1)).thenReturn("200031");
            when(resultSetHeaders.getColumnName(2)).thenReturn("type");
            when(resultSet.getString(2)).thenReturn("non-refrigerated");
            when(resultSetHeaders.getColumnName(3)).thenReturn("container_position_x");
            when(resultSet.getString(3)).thenReturn("1");
            when(resultSetHeaders.getColumnName(4)).thenReturn("container_position_y");
            when(resultSet.getString(4)).thenReturn("2");
            when(resultSetHeaders.getColumnName(5)).thenReturn("container_position_z");
            when(resultSet.getString(5)).thenReturn("3");
            when(resultSetHeaders.getColumnName(6)).thenReturn("payload");
            when(resultSet.getString(6)).thenReturn("28300.0");
            when(resultSetHeaders.getColumnCount()).thenReturn(6);

            //Method call
            List<List<String>> actual = containerSqlStore.getContainerManifest(databaseConnection, "10000001", 1, false);

            //SQL command build test
            String expectedSqlCommand = "SELECT NUM,\n" +
                    "        (CASE WHEN REFRIGERATED_FLAG = 1 THEN 'Refrigerated' ELSE 'Non-refrigerated' END) as \"TYPE\",\n" +
                    "        CONTAINER_POSITION_X, CONTAINER_POSITION_Y, CONTAINER_POSITION_Z,\n" +
                    "        PAYLOAD\n" +
                    "        FROM CONTAINER INNER JOIN CONTAINER_CARGOMANIFEST ON CONTAINER.NUM = CONTAINER_CARGOMANIFEST.CONTAINER_NUM\n" +
                    "        WHERE PARTIAL_CARGO_MANIFEST_ID IN\n" +
                    "          (SELECT ID\n" +
                    "          FROM CARGOMANIFEST_PARTIAL\n" +
                    "          WHERE SHIP_MMSI = 10000001\n" +
                    "            AND STORAGE_IDENTIFICATION = 1\n" +
                    "            AND LOADING_FLAG = 0\n" +
                    "            AND STATUS LIKE 'finished')";
            Assertions.assertEquals(expectedSqlCommand, preparedStatementTest.toString());

            //SQL query result wrapping test
            List<List<String>> expected = new ArrayList<>();
            expected.add(Arrays.asList("container_num", "type", "container_position_x", "container_position_y", "container_position_z", "payload"));
            expected.add(Arrays.asList("200031", "non-refrigerated", "1", "2", "3", "28300.0"));

            Assertions.assertEquals(actual.size(), 2);
            for (int i = 0; i < actual.size(); i++)
                for (int j = 0; j < actual.get(0).size(); j++)
                    Assertions.assertEquals(actual.get(i).get(j), expected.get(i).get(j));

        } catch (Exception ex) {
            Logger.getLogger(ContainerSqlStoreTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void getContainerAuditNull() {
        Assertions.assertTrue(containerSqlStore.getContainerAudit(null, -1, -1).isEmpty());
    }

    @Test
    void getContainerAuditNotNull() {
        try {
            //Setup statement and mock result
            String sqlCommand = "SELECT * FROM Container_AuditTrail\n" +
                    "WHERE container_num = ? AND cargo_manifest_id = ?";

            PreparedStatementTest preparedStatementTest = new PreparedStatementTest(sqlCommand, resultSet);

            when(connection.prepareStatement(sqlCommand)).thenReturn(preparedStatementTest);
            when(resultSet.getMetaData()).thenReturn(resultSetHeaders);
            when(resultSet.next()).thenReturn(true).thenReturn(false);
            when(resultSetHeaders.getColumnName(1)).thenReturn("user_id");
            when(resultSet.getString(1)).thenReturn("MYUSER");
            when(resultSetHeaders.getColumnName(2)).thenReturn("date_time");
            when(resultSet.getString(2)).thenReturn("2021-12-12 23:07:10");
            when(resultSetHeaders.getColumnName(3)).thenReturn("operation_type");
            when(resultSet.getString(3)).thenReturn("INSERT");
            when(resultSetHeaders.getColumnName(4)).thenReturn("container_num");
            when(resultSet.getString(4)).thenReturn("1");
            when(resultSetHeaders.getColumnName(5)).thenReturn("cargo_manifest_id");
            when(resultSet.getString(5)).thenReturn("1");
            when(resultSetHeaders.getColumnCount()).thenReturn(5);

            //Method call
            List<List<String>> actual = containerSqlStore.getContainerAudit(databaseConnection, 1, 1);

            //SQL command build test
            String expectedSqlCommand = "SELECT * FROM Container_AuditTrail\n" +
                    "WHERE container_num = 1 AND cargo_manifest_id = 1";
            Assertions.assertEquals(expectedSqlCommand, preparedStatementTest.toString());

            //SQL query result wrapping test
            List<List<String>> expected = new ArrayList<>();
            expected.add(Arrays.asList("user_id", "date_time", "operation_type", "container_num", "cargo_manifest_id"));
            expected.add(Arrays.asList("MYUSER", "2021-12-12 23:07:10", "INSERT", "1", "1"));

            Assertions.assertEquals(actual.size(), 2);
            for (int i = 0; i < actual.size(); i++)
                for (int j = 0; j < actual.get(0).size(); j++)
                    Assertions.assertEquals(actual.get(i).get(j), expected.get(i).get(j));

        } catch (Exception ex) {
            Logger.getLogger(ContainerSqlStoreTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void getContainerShipmentNull() {
        Assertions.assertEquals(-1, containerSqlStore.getContainerShipment(null, "Client", 1));
        Assertions.assertEquals(-1, containerSqlStore.getContainerShipment(databaseConnection, null, 1));
    }

    @Test
    void getContainerShipmentNotNull() {
        try {
            //Setup main statement and mock result
            String sqlCommand = "SELECT id FROM Shipment\n" +
                    "WHERE container_num = ? AND system_user_code_client = ?";

            PreparedStatementTest preparedStatementTest = new PreparedStatementTest(sqlCommand, resultSet);

            when(connection.prepareStatement(sqlCommand)).thenReturn(preparedStatementTest);
            when(resultSet.next()).thenReturn(true).thenReturn(false);
            when(resultSet.getInt(1)).thenReturn(5);

            //Setup check statement and mock result
            String sqlCommandCheck = "SELECT * FROM Shipment\n" +
                    "WHERE container_num = ? AND system_user_code_client = ?";

            PreparedStatementTest preparedStatementTestCheck = new PreparedStatementTest(sqlCommandCheck, resultSet);

            when(connection.prepareStatement(sqlCommandCheck)).thenReturn(preparedStatementTestCheck);
            when(resultSet.next()).thenReturn(true).thenReturn(false);

            //Method call
            int actual = containerSqlStore.getContainerShipment(databaseConnection, "11111", 1);

            //SQL command build test
            String expectedSqlCommand = "SELECT id FROM Shipment\n" +
                    "WHERE container_num = 1 AND system_user_code_client = 11111";
            Assertions.assertEquals(expectedSqlCommand, preparedStatementTest.toString());

            //SQL query result wrapping test
            int expected = 5;

            Assertions.assertEquals(expected, actual);

        } catch (Exception ex) {
            Logger.getLogger(ContainerSqlStoreTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void getContainerRouteNull() {
        Assertions.assertTrue(containerSqlStore.getContainerRoute(null, -1, -1).isEmpty());
    }

    @Test
    void getContainerRouteNotNull() {
        try {
            //Setup main statement and mock result
            String sqlCommand = "SELECT " +
                    "                (SELECT NAME FROM STORAGE WHERE IDENTIFICATION = STORAGE_IDENTIFICATION) as Location,\n" +
                    "                (CASE WHEN LOADING_FLAG = 1 THEN 'Loaded' ELSE 'Offloaded' END) as Operation,\n" +
                    "                (CASE WHEN SHIP_MMSI IS NOT NULL THEN 'Ship' ELSE 'Truck' END) as \"Mean of Transport\",\n" +
                    "                FINISHING_DATE_TIME as Timestamp\n" +
                    "        FROM CONTAINER_CARGOMANIFEST cc INNER JOIN CARGOMANIFEST_PARTIAL cp on cc.PARTIAL_CARGO_MANIFEST_ID = cp.ID\n" +
                    "        WHERE CONTAINER_NUM = ?\n" +
                    "        AND FINISHING_DATE_TIME BETWEEN ? AND ?\n" +
                    "        ORDER BY FINISHING_DATE_TIME";

            PreparedStatementTest preparedStatementTest = new PreparedStatementTest(sqlCommand, resultSet);
            when(connection.prepareStatement(sqlCommand)).thenReturn(preparedStatementTest);
            when(resultSet.getMetaData()).thenReturn(resultSetHeaders);
            when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
            when(resultSetHeaders.getColumnName(1)).thenReturn("Location");
            when(resultSet.getString(1)).thenReturn("Porto");
            when(resultSetHeaders.getColumnName(2)).thenReturn("Operation");
            when(resultSet.getString(2)).thenReturn("Loaded");
            when(resultSetHeaders.getColumnName(3)).thenReturn("Mean of Transport");
            when(resultSet.getString(3)).thenReturn("Ship");
            when(resultSetHeaders.getColumnName(4)).thenReturn("Timestamp");
            when(resultSet.getString(4)).thenReturn("2021-12-01 10:00:00");
            when(resultSetHeaders.getColumnCount()).thenReturn(4);
            //Setup check statement and mock result
            String sqlCommandCheckDeparture = "SELECT PARTING_DATE FROM SHIPMENT WHERE ID = ?";

            PreparedStatementTest preparedStatementTestCheckDeparture = new PreparedStatementTest(sqlCommandCheckDeparture, resultSet);

            when(connection.prepareStatement(sqlCommandCheckDeparture)).thenReturn(preparedStatementTestCheckDeparture);
            when(resultSet.getTimestamp(1)).thenReturn(Timestamp.valueOf("2021-12-01 10:00:00"));

            ResultSet resultSetArrival = mock(ResultSet.class);
            String sqlCommandCheckArrival = "SELECT ARRIVAL_DATE FROM SHIPMENT WHERE ID = ?";

            PreparedStatementTest preparedStatementTestCheckArrival = new PreparedStatementTest(sqlCommandCheckArrival, resultSetArrival);

            when(connection.prepareStatement(sqlCommandCheckArrival)).thenReturn(preparedStatementTestCheckArrival);
            when(resultSetArrival.next()).thenReturn(false);

            //Method call
            List<List<String>> actual = containerSqlStore.getContainerRoute(databaseConnection, 1, 2);

            //SQL query result wrapping test
            List<List<String>> expected = new ArrayList<>();
            expected.add(Arrays.asList("Location", "Operation", "Mean of Transport", "Timestamp"));
            expected.add(Arrays.asList("Porto", "Loaded", "Ship", "2021-12-01 10:00:00"));

            Assertions.assertEquals(actual.size(), 2);
            for (int i = 0; i < actual.size(); i++)
                for (int j = 0; j < actual.get(0).size(); j++)
                    Assertions.assertEquals(actual.get(i).get(j), expected.get(i).get(j));
        } catch (Exception ex) {
            Logger.getLogger(ContainerSqlStoreTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
}