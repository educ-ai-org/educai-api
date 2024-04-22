package api.educai.enums;

import lombok.Getter;

@Getter
public enum Role {
    TEACHER("TEACHER"),
    STUDENT("STUDENT");

    private String role;

    Role(String role) {
        this.role = role;
    }
}
