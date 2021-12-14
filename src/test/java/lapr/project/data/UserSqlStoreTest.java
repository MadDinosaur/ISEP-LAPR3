package lapr.project.data;

import lapr.project.mappers.dto.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserSqlStoreTest {
    DatabaseConnection databaseConnection;
    Connection connection;
    UserSqlStore userSqlStore;
    UserDTO userDTO;

    @BeforeEach
    void setUp() {
        databaseConnection = mock(DatabaseConnection.class);
        connection = mock(Connection.class);

        try {
            connection.setAutoCommit(false);

            userSqlStore = new UserSqlStore();

            userDTO = new UserDTO(null, "João", "joao@gmail.com", "1");
        } catch (Exception ex) {
            Logger.getLogger(StorageSqlStoreTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void save() {
        try {
            when(databaseConnection.getConnection()).thenReturn(connection);
            String sqlCommand = "insert into user(registration_code, name, email, role) values (?, ?, ?, ?)";
            PreparedStatementTest preparedStatementTest = new PreparedStatementTest(sqlCommand);
            when(connection.prepareStatement(sqlCommand)).thenReturn(preparedStatementTest);

            String sqlCommandSelectId = "select * from user where registration_code = ?";
            ResultSet resultSet = mock(ResultSet.class);

            PreparedStatementTest preparedStatementSelectId = new PreparedStatementTest(sqlCommandSelectId, resultSet);

            when(connection.prepareStatement(sqlCommandSelectId)).thenReturn(preparedStatementSelectId);
            when(resultSet.next()).thenReturn(false);

            boolean result = userSqlStore.save(databaseConnection, userDTO);
            assertTrue(result);

            String expected = "insert into user(registration_code, name, email, role) values (?, ?, ?, ?)";
            Assertions.assertEquals(expected, preparedStatementTest.toString());

            when(resultSet.next()).thenReturn(true);

            sqlCommand = "update user set name = ?, email = ?, role = ? where registration_code = ?";
            preparedStatementTest = new PreparedStatementTest(sqlCommand);
            when(connection.prepareStatement(sqlCommand)).thenReturn(preparedStatementTest);

            userDTO = new UserDTO("11111", "abcd", "joao@gmail.com", "1");
            result = userSqlStore.save(databaseConnection, userDTO);
            assertTrue(result);

            expected = "update user set name = ?, email = ?, role = ? where registration_code = ?";
            Assertions.assertEquals(expected, preparedStatementTest.toString());
        } catch (Exception ex) {
            Logger.getLogger(StorageSqlStoreTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void delete() {
        try {
            when(databaseConnection.getConnection()).thenReturn(connection);

            String sqlCommand = "delete from user where registration_code = ?";
            PreparedStatementTest preparedStatementTest = new PreparedStatementTest(sqlCommand);
            when(connection.prepareStatement(sqlCommand)).thenReturn(preparedStatementTest);

            boolean result = userSqlStore.delete(databaseConnection, userDTO);
            assertTrue(result);

            String expected = "delete from user where registration_code = ?";
            Assertions.assertEquals(expected, preparedStatementTest.toString());
        } catch (Exception ex) {
            Logger.getLogger(StorageSqlStoreTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}