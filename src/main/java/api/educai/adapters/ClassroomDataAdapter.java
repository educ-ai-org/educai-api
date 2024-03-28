package api.educai.adapters;

import api.educai.dto.ClassroomParticipantsDTO;
import api.educai.entities.Classroom;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

public class ClassroomDataAdapter {
    @NotBlank
    @Size(max = 100)
    private String title;
    @NotBlank
    @Size(max = 50)
    private String course;
    @DocumentReference
    private List<ClassroomParticipantsDTO> participants;

    public ClassroomDataAdapter(Classroom classroom, List<ClassroomParticipantsDTO> participants) {
        this.title = classroom.getTitle();
        this.course = classroom.getCourse();
        this.participants = participants;
    }

    public String getTitle() {
        return title;
    }

    public String getCourse() {
        return course;
    }

    public List<ClassroomParticipantsDTO> getParticipants() {
        return participants;
    }
}
