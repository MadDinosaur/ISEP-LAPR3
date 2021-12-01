package lapr.project.data;

import lapr.project.model.Container;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
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

            containerSqlStore = mock(ContainerSqlStore.class);
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
    void deleteNull() {
        Assertions.assertFalse(containerSqlStore.delete(null, null));
    }

    @Test
    void getContainerStatusNull() {
        Assertions.assertTrue(containerSqlStore.getContainerStatus(null, -1).isEmpty());
    }
}