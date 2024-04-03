package api.educai.adapters;

import api.educai.entities.Classroom;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class ClassroomInfoAdapter {
    @Id
    private ObjectId id;
    @NotBlank
    @Size(max = 100)
    private String title;
    @NotBlank
    @Size(max = 50)
    private String course;

    public ClassroomInfoAdapter(Classroom classroom) {
        this.id = classroom.getId();
        this.title = classroom.getTitle();
        this.course = classroom.getCourse();
    }

    public ObjectId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCourse() {
        return course;
    }
}
