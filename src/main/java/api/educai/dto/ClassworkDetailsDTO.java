package api.educai.dto;

import api.educai.entities.Question;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ClassworkDetailsDTO {

    private String id;
    @Size(max = 100)
    private String title;
    @NotNull
    private LocalDate datePosting;
    @NotNull
    private LocalDate endDate;
    @Size(max = 200)
    private String description;
    private List<QuestionDTO> questions = new ArrayList<>();

}
