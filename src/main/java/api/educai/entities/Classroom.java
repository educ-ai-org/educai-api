package api.educai.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Document
public class Classroom {
    @Id
    private ObjectId id;
    @NotBlank
    @Size(max = 100)
    private String title;
    @NotBlank
    @Size(max = 50)
    private String course;
    @DocumentReference
    private List<User> participants = new ArrayList<>();
    @DocumentReference
    private List<Classwork> classworks = new ArrayList<>();
    @DocumentReference
    private List<Post> posts = new ArrayList<>();

    public void addParticipant(User user) {
        participants.add(user);
    }

    public void addClasswork(Classwork classwork) { classworks.add(classwork); }

    public void addPost(Post post) { posts.add(post); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Classroom classroom = (Classroom) obj;
        return Objects.equals(id, classroom.id); // Comparar apenas os IDs para evitar recursão
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
