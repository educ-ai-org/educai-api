package api.educai.dto;

import api.educai.entities.Answer;
import api.educai.entities.Classwork;
import api.educai.entities.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.util.List;

@Data
public class AnswerDTO {

    @NotNull
    private ClassroomParticipantsDTO user;

    @NotNull
    private LocalDate datePosting;
    private List<QuestionAnswerDTO> questionAnswers;

    public AnswerDTO(Answer answer) {
        this.user = new ClassroomParticipantsDTO(answer.getUser());
        this.datePosting = answer.getDatePosting();
        this.questionAnswers = answer.getQuestionAnswers();
    }
}
