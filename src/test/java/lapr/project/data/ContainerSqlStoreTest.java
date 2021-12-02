package lapr.project.data;

import lapr.project.model.Container;
import oracle.ucp.util.Pair;
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
            String sqlCommand = "select * from (" +
                    "    select container.num as container_num," +
                    "           (case when cargomanifest.loading_flag = 1 then 'ship' else 'port' end) as location_type," +
                    "           (case when cargomanifest.loading_flag = 1 then (select name from ship where mmsi = cargomanifest.ship_mmsi) else (select name from storage where identification = cargomanifest.storage_identification) end) as location_name," +
                    "    from container" +
                    "    inner join container_cargomanifest on container_cargomanifest.container_num = container.num" +
                    "    inner join cargomanifest on container_cargomanifest.cargo_manifest_id = cargomanifest.id" +
                    "    where container.num = ? and cargomanifest.finishing_date_time is not null" +
                    "    order by cargomanifest.finishing_date_time desc)" +
                    "where rownum = 1";

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
            List<Pair<String, String>> actual = containerSqlStore.getContainerStatus(databaseConnection, 200031);

            //SQL command build test
            String expectedSqlCommand = "select * from (" +
                    "    select container.num as container_num," +
                    "           (case when cargomanifest.loading_flag = 1 then 'ship' else 'port' end) as location_type," +
                    "           (case when cargomanifest.loading_flag = 1 then (select name from ship where mmsi = cargomanifest.ship_mmsi) else (select name from storage where identification = cargomanifest.storage_identification) end) as location_name," +
                    "    from container" +
                    "    inner join container_cargomanifest on container_cargomanifest.container_num = container.num" +
                    "    inner join cargomanifest on container_cargomanifest.cargo_manifest_id = cargomanifest.id" +
                    "    where container.num = 200031 and cargomanifest.finishing_date_time is not null" +
                    "    order by cargomanifest.finishing_date_time desc)" +
                    "where rownum = 1";
            Assertions.assertEquals(expectedSqlCommand, preparedStatementTest.toString());

            //SQL query result wrapping test
            List<Pair<String, String>> expected = new ArrayList<>();
            expected.add(new Pair<>("container_num", "200031"));
            expected.add(new Pair<>("location_type", "ship"));
            expected.add(new Pair<>("location_name", "100000001"));

            Assertions.assertEquals(actual.size(), expected.size());
            for(int i = 0; i < actual.size(); i++) {
                Assertions.assertEquals(actual.get(i).get1st(), expected.get(i).get1st());
                Assertions.assertEquals(actual.get(i).get2nd(), expected.get(i).get2nd());
            }
        } catch (Exception ex) {
            Logger.getLogger(ContainerSqlStoreTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void getNextUnloadingManifestNull() {
        Assertions.assertTrue(containerSqlStore.getNextUnloadingManifest(null, null, -1).isEmpty());
    }

    @Test
    void getNextUnloadingManifestNotNull() {
        try {
            //Setup statement and mock result
            String sqlCommand = "select num as container_num," +
                    "       (case when refrigerated_flag = 1 then 'refrigerated' else 'non-refrigerated' end) as type," +
                    "       container_position_x, container_position_y, container_position_z," +
                    "       payload" +
                    "from container inner join container_cargomanifest on container.num = container_cargomanifest.container_num" +
                    "where cargo_manifest_id in" +
                    "      (select cargo_manifest_id" +
                    "      from cargomanifest" +
                    "      where ship_mmsi = ?" +
                    "        and cargomanifest.storage_identification = ?" +
                    "        and loading_flag = 0" +
                    "        and finishing_date_time is null)";

            PreparedStatementTest preparedStatementTest = new PreparedStatementTest(sqlCommand, resultSet);

            when(connection.prepareStatement(sqlCommand)).thenReturn(preparedStatementTest);
            when(resultSet.getMetaData()).thenReturn(resultSetHeaders);
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
            List<List<String>> actual = containerSqlStore.getNextUnloadingManifest(databaseConnection, "10000001", 1);

            //SQL command build test
            String expectedSqlCommand = "select num as container_num," +
                    "       (case when refrigerated_flag = 1 then 'refrigerated' else 'non-refrigerated' end) as type," +
                    "       container_position_x, container_position_y, container_position_z," +
                    "       payload" +
                    "from container inner join container_cargomanifest on container.num = container_cargomanifest.container_num" +
                    "where cargo_manifest_id in" +
                    "      (select cargo_manifest_id" +
                    "      from cargomanifest" +
                    "      where ship_mmsi = 10000001" +
                    "        and cargomanifest.storage_identification = 1" +
                    "        and loading_flag = 0" +
                    "        and finishing_date_time is null)";
            Assertions.assertEquals(expectedSqlCommand, preparedStatementTest.toString());

            //SQL query result wrapping test
            List<List<String>> expected = new ArrayList<>();
            expected.add(Arrays.asList("container_num", "type", "container_position_x", "container_position_y", "container_position_z", "payload"));
            expected.add(Arrays.asList("200031", "non-refrigerated", "1", "2", "3", "28300.0"));

            Assertions.assertEquals(actual.size(), expected.size());
            for (int i = 0; i < actual.size(); i++)
                for (int j = 0; j < actual.get(0).size(); j++)
                    Assertions.assertEquals(actual.get(i).get(j), expected.get(i).get(j));

        } catch (Exception ex) {
            Logger.getLogger(ContainerSqlStoreTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
}