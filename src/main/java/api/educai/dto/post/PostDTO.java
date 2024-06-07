package api.educai.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PostDTO {
    private String id;
    @NotBlank
    @Size(max = 100)
    private String title;
    @NotBlank
    @Size(max = 100)
    private String description;
    private String file;
    private String originalFileName;
    private LocalDate datePosting;
}
