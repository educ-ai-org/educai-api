package api.educai.adapters;

import api.educai.entities.Classroom;
import api.educai.entities.User;

import java.util.List;

public class StudentClassroomsAdapter extends ClassroomInfoAdapter{
    private int pendingTasks = 0;

    public StudentClassroomsAdapter(Classroom classroom, int pendingTasks) {
        super(classroom);
        this.pendingTasks = pendingTasks;
    }

    public int getPendingTasks() {
        return pendingTasks;
    }
}
