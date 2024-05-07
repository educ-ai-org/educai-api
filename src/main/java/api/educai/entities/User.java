package api.educai.entities;

import api.educai.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Document
public class User {
    @Id
    private ObjectId id;
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotBlank
    @Email
    private String email;
    @Size(min = 8)
    private String password;
    @NotNull
    private Role role;
    @DocumentReference
    private List<Classroom> classrooms = new ArrayList<>();
    private Integer score;

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.score = 0;
    }

    public void encodePassword() {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public void incrementScore(Integer score) {
        this.score += score;
    }

    public boolean isUserEnrolledInClassroom(ObjectId classroomId) {
        return classrooms.stream().anyMatch(classroom -> classroom.getId().equals(classroomId));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(id, user.id); // Comparar apenas os IDs para evitar recursão
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
