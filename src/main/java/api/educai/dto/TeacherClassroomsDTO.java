package api.educai.dto;

import api.educai.entities.Classroom;
import lombok.Getter;

@Getter
public class TeacherClassroomsDTO extends ClassroomInfoDTO {
    private int studentsCount;

    public TeacherClassroomsDTO(Classroom classroom, int studentsCount) {
        super(classroom);
        this.studentsCount = studentsCount;
    }

}
