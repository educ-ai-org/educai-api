package api.educai.adapters;

import api.educai.entities.Classroom;

public class TeacherClassroomsAdapter extends ClassroomInfoAdapter{
    private int studentsCount;

    public TeacherClassroomsAdapter(Classroom classroom, int studentsCount) {
        super(classroom);
        this.studentsCount = studentsCount;
    }

    public int getStudentsCount() {
        return studentsCount;
    }
}
