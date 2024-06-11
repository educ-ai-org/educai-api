package api.educai.dto.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class QuestionAnswerResponseDTO {

    @NotBlank
    private String optionKey;
    @NotNull
    private String questionId;
}
