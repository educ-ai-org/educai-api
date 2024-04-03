package api.educai.entities;

import api.educai.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Document
public class User {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
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

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public ObjectId getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void encryptPassword() {
        password = passwordEncoder.encode(password);
    }

    public List<Classroom> getClassrooms() {
        return classrooms;
    }

    public boolean passwordIsValid(String password) {
        return passwordEncoder.matches(password, this.password);
    }

    public boolean isUserEnrolledInClassroom(ObjectId classroomId) {
        return classrooms.stream().anyMatch(classroom -> classroom.getId().equals(classroomId));
    }
}
