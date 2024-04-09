package api.educai.dto;

public class NewStudentEmailDTO {
    private String student_email;
    private String student_password;
    private String classroom_name;

    public NewStudentEmailDTO(String student_email, String student_password, String classroom_name) {
        this.student_email = student_email;
        this.student_password = student_password;
        this.classroom_name = classroom_name;
    }

    public String getStudent_email() {
        return student_email;
    }

    public String getStudent_password() {
        return student_password;
    }

    public String getClassroom_name() {
        return classroom_name;
    }
}
