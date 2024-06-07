package api.educai.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewStudentEmailDTO {
    private String student_email;
    private String student_password;
    private String classroom_name;
}
