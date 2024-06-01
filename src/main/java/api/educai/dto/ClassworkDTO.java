package api.educai.dto;

import api.educai.entities.Classwork;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ClassworkDTO {

    private String id;
    @Size(max = 100)
    private String title;
    @NotNull
    private LocalDate datePosting;
    @NotNull
    private LocalDate endDate;
    @Size(max = 200)
    private String description;
    @NotNull
    private Integer totalQuestions;
    private Integer totalAnswers;

    public ClassworkDTO(Classwork classwork) {
        this.id = String.valueOf(classwork.getId());
        this.title = classwork.getTitle();
        this.datePosting = classwork.getDatePosting();
        this.endDate = classwork.getEndDate();
        this.description = classwork.getDescription();
        this.totalQuestions = classwork.getQuestions().size();
        this.totalAnswers = classwork.getAnswers().size();
    }
}
