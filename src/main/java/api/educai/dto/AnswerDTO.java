package api.educai.dto;

import api.educai.entities.Answer;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AnswerDTO {

    private String id;
    @NotNull
    private ClassroomParticipantsDTO user;
    @NotNull
    private LocalDate datePosting;
    private List<QuestionAnswerDTO> questionAnswers;

    public AnswerDTO(Answer answer) {
        this.id = String.valueOf(answer.getId());
        this.user = new ClassroomParticipantsDTO(answer.getUser());
        this.datePosting = answer.getDatePosting();
        this.questionAnswers = answer.getQuestionAnswers();
    }
}
