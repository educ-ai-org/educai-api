package api.educai.dto;

import api.educai.entities.User;
import api.educai.enums.Role;
import lombok.Getter;

@Getter
public class UserDTO {
    private String name;
    private String email;
    private Role role;

    public UserDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

    public UserDTO(String name, String email, Role role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

}
