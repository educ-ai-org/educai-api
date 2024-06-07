package api.educai.dto.user;

import api.educai.dto.classroom.ClassroomInfoDTO;
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
