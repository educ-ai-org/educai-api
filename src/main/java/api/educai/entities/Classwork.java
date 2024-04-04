package api.educai.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Document
public class Classwork {

    @Id
    private ObjectId id;
    @NotBlank
    @Size(max = 100)
    private String title;
    @NotNull
    private LocalDate datePosting;
    @NotNull
    private LocalDate endDate;
    @NotBlank
    @Size(max = 200)
    private String description;
    @DocumentReference
    private List<Question> questions = new ArrayList<>();
    @DocumentReference
    private List<Answer> answers = new ArrayList<>();

}
