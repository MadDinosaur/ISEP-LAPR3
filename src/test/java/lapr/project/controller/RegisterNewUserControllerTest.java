package lapr.project.controller;

import lapr.project.data.ConnectionFactory;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.UserSqlStore;
import lapr.project.mappers.dto.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterNewUserControllerTest {

    @Test
    void registerNewUser() {
        RegisterNewUserController ctrl = mock(RegisterNewUserController.class);
        UserSqlStore userSqlStore = mock(UserSqlStore.class);

        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        DatabaseConnection connection = mock(DatabaseConnection.class);

        UserDTO userDTO = new UserDTO(null, "João", "joao@gmail.com", "1");

        when(connectionFactory.getDatabaseConnection()).thenReturn(connection);

        when(userSqlStore.registerNewUserToDB(userDTO)).thenReturn(11111);

        when(ctrl.registerNewUser(userDTO)).thenReturn(11111);

        assertEquals(11111, ctrl.registerNewUser(userDTO));
    }
}