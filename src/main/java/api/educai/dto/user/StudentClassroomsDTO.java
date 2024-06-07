package api.educai.dto.user;

import api.educai.dto.classroom.ClassroomInfoDTO;
import api.educai.entities.Classroom;
import lombok.Getter;

@Getter
public class StudentClassroomsDTO extends ClassroomInfoDTO {
    private String nextSubmission = "";

    public StudentClassroomsDTO(Classroom classroom, String nextSubmission) {
        super(classroom);
        this.nextSubmission = nextSubmission;
    }

}
