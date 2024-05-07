package api.educai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class QuestionAnswerDTO {

    @NotBlank
    private String optionKey;
    @NotNull
    private ObjectId questionId;
}
