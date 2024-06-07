package api.educai.dto;

import lombok.Data;

@Data
public class UserAnswerStatusDTO {
    private String userId;
    private boolean hasAnswered;
}