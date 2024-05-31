package api.educai.dto;

import api.educai.entities.Classroom;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ClassroomInfoDTO {

    private String id;
    @NotBlank
    @Size(max = 100)
    private String title;
    @NotBlank
    @Size(max = 50)
    private String course;
    private List<PostDTO> posts;
    private List<ClassworkDTO> classworks;
    private List<UserDTO> participants;

    public ClassroomInfoDTO(Classroom classroom) {
        this.id = classroom.getId().toString();
        this.title = classroom.getTitle();
        this.course = classroom.getCourse();
    }

}
