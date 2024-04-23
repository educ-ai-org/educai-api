package api.educai.dto;

import api.educai.dto.ClassroomInfoDTO;
import api.educai.entities.Classroom;
import lombok.Getter;

@Getter
public class StudentClassroomsDTO extends ClassroomInfoDTO {
    private int pendingTasks = 0;

    public StudentClassroomsDTO(Classroom classroom, int pendingTasks) {
        super(classroom);
        this.pendingTasks = pendingTasks;
    }

}
