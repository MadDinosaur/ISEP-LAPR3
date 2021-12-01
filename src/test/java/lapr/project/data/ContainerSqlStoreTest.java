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
    PreparedStatement preparedStatement;
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
                    .log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void deleteNull() {
        Assertions.assertFalse(containerSqlStore.delete(null, null));
    }

    @Test
    void getContainerStatusNull() {
        Assertions.assertTrue(containerSqlStore.getContainerStatus(null, -1).isEmpty());
    }
}