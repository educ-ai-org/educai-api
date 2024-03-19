package api.educai.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PatchUserEmailAndName {
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotBlank
    @Email
    private String email;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
