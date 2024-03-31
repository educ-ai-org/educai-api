package api.educai.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document
public class Post {
    @Id
    private ObjectId id;
    @DocumentReference
    @NotBlank
    private ObjectId classroomId;
    @NotBlank
    @Size(max = 100)
    private String title;
    @Size(max = 100)
    @NotBlank
    private String type;
    @NotBlank
    private String url;
}
