package api.educai.dto;

import api.educai.entities.Classroom;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Getter
public class ClassroomInfoDTO {
    @Id
    private ObjectId id;
    @NotBlank
    @Size(max = 100)
    private String title;
    @NotBlank
    @Size(max = 50)
    private String course;

    public ClassroomInfoDTO(Classroom classroom) {
        this.id = classroom.getId();
        this.title = classroom.getTitle();
        this.course = classroom.getCourse();
    }

}
