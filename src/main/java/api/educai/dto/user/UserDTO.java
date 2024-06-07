package api.educai.dto.user;

import api.educai.entities.User;
import api.educai.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String name;
    private String email;
    private Role role;

    public UserDTO(User user) {
        this.id = String.valueOf(user.getId());
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
