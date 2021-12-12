package lapr.project.data;

import lapr.project.model.Container;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
    Container container = new Container(200031,7,"22G1",30480.0,2180.0,28300.0,33.1,false);

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
            String sqlCommand = "SELECT * FROM (\n" +
                    "    SELECT CONTAINER.NUM as CONTAINER_NUM,\n" +
                    "           (CASE WHEN CARGOMANIFEST.LOADING_FLAG = 1 THEN 'Ship' ELSE 'Port' END) as LOCATION_TYPE,\n" +
                    "           (CASE WHEN CARGOMANIFEST.LOADING_FLAG = 1 THEN (SELECT NAME FROM SHIP WHERE MMSI = CARGOMANIFEST.SHIP_MMSI) ELSE (SELECT NAME FROM STORAGE WHERE IDENTIFICATION = CARGOMANIFEST.STORAGE_IDENTIFICATION) END) as LOCATION_NAME\n" +
                    "    FROM CONTAINER\n" +
                    "    INNER JOIN CONTAINER_CARGOMANIFEST ON CONTAINER_CARGOMANIFEST.CONTAINER_NUM = CONTAINER.NUM\n" +
                    "    INNER JOIN CARGOMANIFEST ON CONTAINER_CARGOMANIFEST.CARGO_MANIFEST_ID = CARGOMANIFEST.ID\n" +
                    "    WHERE CONTAINER.NUM = 200031 AND CARGOMANIFEST.FINISHING_DATE_TIME IS NOT NULL AND CARGOMANIFEST.LOADING_FLAG IS NOT NULL\n" +
                    "    ORDER BY CARGOMANIFEST.FINISHING_DATE_TIME DESC)\n" +
                    "WHERE ROWNUM = 1";

            PreparedStatementTest preparedStatementTest = new PreparedStatementTest(sqlCommand, resultSet);

            when(connection.prepareStatement(sqlCommand)).thenReturn(preparedStatementTest);
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
            String expectedSqlCommand = "SELECT * FROM (\n" +
                    "    SELECT CONTAINER.NUM as CONTAINER_NUM,\n" +
                    "           (CASE WHEN CARGOMANIFEST.LOADING_FLAG = 1 THEN 'Ship' ELSE 'Port' END) as LOCATION_TYPE,\n" +
                    "           (CASE WHEN CARGOMANIFEST.LOADING_FLAG = 1 THEN (SELECT NAME FROM SHIP WHERE MMSI = CARGOMANIFEST.SHIP_MMSI) ELSE (SELECT NAME FROM STORAGE WHERE IDENTIFICATION = CARGOMANIFEST.STORAGE_IDENTIFICATION) END) as LOCATION_NAME\n" +
                    "    FROM CONTAINER\n" +
                    "    INNER JOIN CONTAINER_CARGOMANIFEST ON CONTAINER_CARGOMANIFEST.CONTAINER_NUM = CONTAINER.NUM\n" +
                    "    INNER JOIN CARGOMANIFEST ON CONTAINER_CARGOMANIFEST.CARGO_MANIFEST_ID = CARGOMANIFEST.ID\n" +
                    "    WHERE CONTAINER.NUM = 200031 AND CARGOMANIFEST.FINISHING_DATE_TIME IS NOT NULL AND CARGOMANIFEST.LOADING_FLAG IS NOT NULL\n" +
                    "    ORDER BY CARGOMANIFEST.FINISHING_DATE_TIME DESC)\n" +
                    "WHERE ROWNUM = 1";
            Assertions.assertEquals(expectedSqlCommand, preparedStatementTest.toString());

            //SQL query result wrapping test
            List<String> expected = Arrays.asList("200031, ship, 100000001");

            Assertions.assertEquals(actual.size(), 0);
            for(int i = 0; i < actual.size(); i++) {
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
            String sqlCommand = "select num,\n" +
                    "       (case when refrigerated_flag = 1 then 'refrigerated' else 'non-refrigerated' end) as type,\n" +
                    "       container_position_x, container_position_y, container_position_z,\n" +
                    "       payload\n" +
                    "from container inner join container_cargomanifest on container.num = container_cargomanifest.container_num\n" +
                    "where cargo_manifest_id in\n" +
                    "      (select id\n" +
                    "      from cargomanifest\n" +
                    "      where ship_mmsi = 10000001\n" +
                    "        and cargomanifest.storage_identification = 1\n" +
                    "        and loading_flag = 0\n" +
                    "        and finishing_date_time is null)";

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
            String expectedSqlCommand = "select num,\n" +
                    "       (case when refrigerated_flag = 1 then 'refrigerated' else 'non-refrigerated' end) as type,\n" +
                    "       container_position_x, container_position_y, container_position_z,\n" +
                    "       payload\n" +
                    "from container inner join container_cargomanifest on container.num = container_cargomanifest.container_num\n" +
                    "where cargo_manifest_id in\n" +
                    "      (select id\n" +
                    "      from cargomanifest\n" +
                    "      where ship_mmsi = 10000001\n" +
                    "        and cargomanifest.storage_identification = 1\n" +
                    "        and loading_flag = 0\n" +
                    "        and finishing_date_time is null)";
            Assertions.assertEquals(expectedSqlCommand, preparedStatementTest.toString());

            //SQL query result wrapping test
            List<List<String>> expected = new ArrayList<>();
            expected.add(Arrays.asList("container_num", "type", "container_position_x", "container_position_y", "container_position_z", "payload"));
            expected.add(Arrays.asList("200031", "non-refrigerated", "1", "2", "3", "28300.0"));

            Assertions.assertEquals(actual.size(), 0);
            for (int i = 0; i < actual.size(); i++)
                for (int j = 0; j < actual.get(0).size(); j++)
                    Assertions.assertEquals(actual.get(i).get(j), expected.get(i).get(j));

        } catch (Exception ex) {
            Logger.getLogger(ContainerSqlStoreTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
}