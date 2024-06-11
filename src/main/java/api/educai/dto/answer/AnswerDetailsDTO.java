package api.educai.dto.answer;

import api.educai.dto.classroom.ClassroomParticipantsDTO;
import api.educai.dto.classwork.ClassworkDTO;
import api.educai.dto.classwork.ClassworkDetailsDTO;
import api.educai.entities.Answer;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class AnswerDetailsDTO {

    private String id;
    @NotNull
    private ClassroomParticipantsDTO user;
    @NotNull
    private LocalDate datePosting;
    private ClassworkDetailsDTO classwork;
    private List<QuestionAnswerResponseDTO> questionAnswers;

}
