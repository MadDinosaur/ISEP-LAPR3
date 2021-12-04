package lapr.project.data;

import lapr.project.model.Coordinate;
import lapr.project.model.Storage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        try {
            when(databaseConnection.getConnection()).thenReturn(connection);
            String sqlCommand = "insert into storage(storage_type_id, name, continent, country, latitude, longitude, identification) values (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatementTest preparedStatementTest = new PreparedStatementTest(sqlCommand);
            when(connection.prepareStatement(sqlCommand)).thenReturn(preparedStatementTest);

            String sqlCommandSelectId = "select * from storage where identification = ?";
            ResultSet resultSet = mock(ResultSet.class);
            PreparedStatementTest preparedStatementSelectId = new PreparedStatementTest(sqlCommandSelectId, resultSet);
            when(connection.prepareStatement(sqlCommandSelectId)).thenReturn(preparedStatementSelectId);
            when(resultSet.next()).thenReturn(false);

            boolean result = storageSqlStore.save(databaseConnection, storage);
            assertTrue(result);

            String expected = "insert into storage(storage_type_id, name, continent, country, latitude, longitude, identification) values (1, name, continent, country, 20.0, 10.0, 1)";
            Assertions.assertEquals(expected, preparedStatementTest.toString());

            when(resultSet.next()).thenReturn(true);

            sqlCommand = "update storage set storage_type_id = ?, name = ?, continent = ?, country = ?, latitude = ?, longitude = ? where identification = ?";
            preparedStatementTest = new PreparedStatementTest(sqlCommand);
            when(connection.prepareStatement(sqlCommand)).thenReturn(preparedStatementTest);

            storage.setName("abcd");
            result = storageSqlStore.save(databaseConnection, storage);
            assertTrue(result);

            expected = "update storage set storage_type_id = 1, name = abcd, continent = continent, country = country, latitude = 20.0, longitude = 10.0 where identification = 1";
            Assertions.assertEquals(expected, preparedStatementTest.toString());
        } catch (Exception ex) {
            Logger.getLogger(StorageSqlStoreTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void delete() {
        try {
            when(databaseConnection.getConnection()).thenReturn(connection);

            String sqlCommand = "delete from storage where identification = ?";
            PreparedStatementTest preparedStatementTest = new PreparedStatementTest(sqlCommand);
            when(connection.prepareStatement(sqlCommand)).thenReturn(preparedStatementTest);

            boolean result = storageSqlStore.delete(databaseConnection, storage);
            assertTrue(result);

            String expected = "delete from storage where identification = 1";
            Assertions.assertEquals(expected, preparedStatementTest.toString());
        } catch (Exception ex) {
            Logger.getLogger(StorageSqlStoreTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}