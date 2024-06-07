package api.educai.dto.classroom;

import api.educai.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClassroomParticipantsDTO {

    private String id;
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotBlank
    @Email
    private String email;

    public ClassroomParticipantsDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
    }

}
