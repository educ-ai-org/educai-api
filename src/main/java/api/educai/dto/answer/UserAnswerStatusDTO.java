package api.educai.dto.answer;

import api.educai.dto.user.UserDTO;
import lombok.Data;

@Data
public class UserAnswerStatusDTO {
    private UserDTO user;
    private boolean hasAnswered;
    private Double correctPercentage;
}