package api.educai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class NewPostDTO {
    @NotBlank
    @Size(max = 100)
    private String title;
    @Size(max = 100)
    private String description;
    @NotNull
    private LocalDate datePosting;
    @NotBlank
    private String classroomId;

    public NewPostDTO(String title, String description, LocalDate datePosting, String classroomId) {
        this.title = title;
        this.description = description;
        this.datePosting = datePosting;
        this.classroomId = classroomId;
    }
}
