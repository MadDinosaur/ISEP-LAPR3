package lapr.project.controller;

import lapr.project.data.ConnectionFactory;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.UserSqlStore;
import lapr.project.mappers.dto.UserDTO;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterNewUserControllerTest {

    @Test
    void registerNewUser(){
        RegisterNewUserController ctrl = mock(RegisterNewUserController.class);
        UserSqlStore userSqlStore = mock(UserSqlStore.class);

        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        DatabaseConnection connection = mock(DatabaseConnection.class);

        UserDTO userDTO = new UserDTO(null, "João", "joao@gmail.com", "1");

        when(connectionFactory.getDatabaseConnection()).thenReturn(connection);

        when(userSqlStore.registerNewUserToDB(connection, userDTO)).thenReturn("A23VA");

        when(ctrl.registerNewUser(userDTO)).thenReturn("A23VA");

        assertEquals("A23VA", ctrl.registerNewUser(userDTO));
    }
}