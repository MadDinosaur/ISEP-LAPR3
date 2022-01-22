package lapr.project.mappers.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    private final String registrationCode = "12345";
    private final String name = "User";
    private final String email = "user@email.com";
    private final String role = "Captain";

    private final UserDTO userDTO = new UserDTO(registrationCode,name,email,role);

    @Test
    void getRegistrationCode() {
        assertNotNull(userDTO.getRegistrationCode());
        assertEquals(userDTO.getRegistrationCode(), registrationCode);
    }

    @Test
    void getUserName() {
        assertNotNull(userDTO.getUserName());
        assertEquals(userDTO.getUserName(), name);
    }

    @Test
    void getEmail() {
        assertNotNull(userDTO.getEmail());
        assertEquals(userDTO.getEmail(),email);
    }

    @Test
    void getRole() {
        assertNotNull(userDTO.getRole());
        assertEquals(userDTO.getRole(), role);
    }

    @Test
    void setRegistrationCode(){
        userDTO.setRegistrationCode("54321");
        assertNotNull(userDTO.getRegistrationCode());
        assertEquals(userDTO.getRegistrationCode(),"54321");
    }
}