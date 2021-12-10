package lapr.project.mappers.dto;

public class UserDTO {
    private String registrationCode;

    private String name;

    private String email;

    private String role;

    public UserDTO(String registrationCode, String name, String email, String role) {
        this.registrationCode = registrationCode;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public String getUserName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
