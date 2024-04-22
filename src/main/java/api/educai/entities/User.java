package api.educai.entities;

import api.educai.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Document
public class User implements UserDetails {
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
    private Integer score;

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.role = role;
        this.score = 0;
    }

    public void incrementScore(Integer score) {
        this.score += score;
    }

    public boolean isUserEnrolledInClassroom(ObjectId classroomId) {
        return classrooms.stream().anyMatch(classroom -> classroom.getId().equals(classroomId));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == Role.TEACHER) return List.of(new SimpleGrantedAuthority("ROLE_TEACHER"));

        return List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
