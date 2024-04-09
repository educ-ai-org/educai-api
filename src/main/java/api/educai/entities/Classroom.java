package api.educai.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

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
    private List<Post> posts = new ArrayList<>();


    public ObjectId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCourse() {
        return course;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public List<Post> getPosts() { return posts; }

    public void addParticipant(User user) {
        participants.add(user);
    }
    public void addPost(Post post) { posts.add(post); }
}
