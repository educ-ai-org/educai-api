package api.educai.entities;

import api.educai.dto.QuestionAnswerDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.util.List;

@Data
@Document
public class Answer {

    @Id
    private ObjectId id;

    @DocumentReference
    private User user;

    @DocumentReference
    private Classwork classwork;

    @NotNull
    private LocalDate datePosting;

    private List<QuestionAnswerDTO> questionAnswers;

}
