package api.educai.dto.answer;

import lombok.Data;

@Data
public class UserAnswerStatusDTO {
    private String userId;
    private boolean hasAnswered;
}