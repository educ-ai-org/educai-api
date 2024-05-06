package api.educai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PatchClassroomTitleAndCourse {
    @Size(max = 100)
    private String title;
    @Size(max = 50)
    private String course;
}
