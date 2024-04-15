package api.educai.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Data
@Document
public class Question {

    @Id
    private ObjectId id;

    @NotBlank
    @Size(max = 200)
    private String description;

    @DocumentReference
    private List<Option> options = new ArrayList<>();

    @NotNull
    private String correctAnswerKey;

}
