package api.educai.adapters;

import api.educai.entities.User;
import api.educai.enums.Role;
import org.bson.types.ObjectId;

public class UserAdapter {
    private String name;
    private String email;
    private Role role;

    public UserAdapter(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

    public UserAdapter(String name, String email, Role role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}
