package api.educai.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PatchPost {
    @NotBlank
    @Size(max = 100)
    private String title;
    @NotBlank
    @Size(max = 100)
    private String description;
    private String url;
    private LocalDate datePosting;
}
