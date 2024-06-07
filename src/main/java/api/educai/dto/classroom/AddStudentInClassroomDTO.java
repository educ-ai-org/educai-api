package api.educai.dto.classroom;

public class AddStudentInClassroomDTO {
    private String student_email;
    private String classroom_name;

    public AddStudentInClassroomDTO(String student_email, String classroom_name) {
        this.student_email = student_email;
        this.classroom_name = classroom_name;
    }

    public String getStudent_email() {
        return student_email;
    }

    public String getClassroom_name() {
        return classroom_name;
    }
}
