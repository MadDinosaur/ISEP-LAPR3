package lapr.project.data;

import lapr.project.model.Coordinate;
import lapr.project.model.Storage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StorageSqlStoreTest {
    DatabaseConnection databaseConnection;
    Connection connection;
    StorageSqlStore storageSqlStore;
    Storage storage;

    @BeforeEach
    void setUp() {
        databaseConnection = mock(DatabaseConnection.class);
        connection = mock(Connection.class);

        try {
            connection.setAutoCommit(false);

            storageSqlStore = new StorageSqlStore();

            Coordinate coord = new Coordinate(10, 20);

            storage = new Storage(1, "name", "continent", "country", coord);
        } catch (Exception ex) {
            Logger.getLogger(StorageSqlStoreTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void save() {
        when(databaseConnection.getConnection()).thenReturn(connection);
        try {
            String sqlCommand = "insert into storage(storage_type_id, name, continent, country, latitude, longitude, identification) values (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatementTest preparedStatementTest = new PreparedStatementTest(sqlCommand);
            boolean result = storageSqlStore.save(databaseConnection, storage);
            assertTrue(result);

            String expected = "insert into storage(storage_type_id, name, continent, country, latitude, longitude, identification) values (1, name, continent, country, 20, 10, 1)";
            Assertions.assertEquals(expected, preparedStatementTest.toString());

            Logger.getLogger(StorageSqlStoreTest.class.getName()).log(Level.INFO, "Added Storage!");

            sqlCommand = "update storage set storage_type_id = ?, name = ?, continent = ?, country = ?, latitude = ?, longitude = ? where identification = ?";
            preparedStatementTest = new PreparedStatementTest(sqlCommand);

            storage.setName("abcd");
            result = storageSqlStore.save(databaseConnection, storage);
            assertTrue(result);


            expected = "update storage set storage_type_id = 1, name = abcd, continent = continent, country = country, latitude = latitude, longitude = longitude where identification = 1";
            Assertions.assertEquals(expected, preparedStatementTest.toString());

            Logger.getLogger(StorageSqlStoreTest.class.getName()).log(Level.INFO, "Changed Storage!");

            when(storageSqlStore.save(databaseConnection, storage)).thenReturn(false);
            result = storageSqlStore.save(databaseConnection, storage);
            assertFalse(result);

        } catch (Exception ex) {
            Logger.getLogger(StorageSqlStoreTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void delete() {
        try {
            when(storageSqlStore.delete(databaseConnection, storage)).thenReturn(true);
            boolean result = storageSqlStore.delete(databaseConnection, storage);
            assertTrue(result);

            Logger.getLogger(StorageSqlStoreTest.class.getName()).log(Level.INFO, "Deleted Storage!");

            when(storageSqlStore.delete(databaseConnection, storage)).thenReturn(false);
            result = storageSqlStore.delete(databaseConnection, storage);
            assertFalse(result);

        } catch (Exception ex) {
            Logger.getLogger(StorageSqlStoreTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void getStorageDataFromDataBase() {

    }
}