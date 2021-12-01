package lapr.project.data;

import lapr.project.model.Container;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ContainerSqlStoreTest {
    ContainerSqlStore containerSqlStore;
    DatabaseConnection databaseConnection;
    Connection connection;
    Container container = new Container(200031,7,"22G1",30480.0,2180.0,28300.0,33.1,false);

    @BeforeEach
    void setUp() {
        containerSqlStore = new ContainerSqlStore();

        databaseConnection = mock(DatabaseConnection.class);

        connection = mock(Connection.class);

        try {
            connection.setAutoCommit(false);
        } catch (Exception ex) {
            Logger.getLogger(ContainerSqlStoreTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void saveNull() {
        Assertions.assertFalse(containerSqlStore.save(null, null));
    }

    @Test
    void saveNotNull() {
        when(databaseConnection.getConnection()).thenReturn(connection);
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
        when(databaseConnection.getConnection()).thenReturn(connection);
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
        when(databaseConnection.getConnection()).thenReturn(connection);
        try {
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
            PreparedStatementTest preparedStatementTest = new PreparedStatementTest(sqlCommand);

            when(connection.prepareStatement(sqlCommand)).thenReturn(preparedStatementTest);
            containerSqlStore.getContainerStatus(databaseConnection, 200031);

            String expected = "select * from (" +
                    "    select container.num as container_num," +
                    "           (case when cargomanifest.loading_flag = 1 then 'ship' else 'port' end) as location_type," +
                    "           (case when cargomanifest.loading_flag = 1 then (select name from ship where mmsi = cargomanifest.ship_mmsi) else (select name from storage where identification = cargomanifest.storage_identification) end) as location_name," +
                    "    from container" +
                    "    inner join container_cargomanifest on container_cargomanifest.container_num = container.num" +
                    "    inner join cargomanifest on container_cargomanifest.cargo_manifest_id = cargomanifest.id" +
                    "    where container.num = 200031 and cargomanifest.finishing_date_time is not null" +
                    "    order by cargomanifest.finishing_date_time desc)" +
                    "where rownum = 1";
            Assertions.assertEquals(expected, preparedStatementTest.toString());
        } catch (Exception ex) {
            Logger.getLogger(ContainerSqlStoreTest.class.getName())
                    .log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}