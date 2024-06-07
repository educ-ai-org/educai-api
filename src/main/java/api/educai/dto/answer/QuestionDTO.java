package api.educai.dto.answer;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class QuestionDTO {

    private String id;
    private String description;
    private List<OptionDTO> options = new ArrayList<>();
    private String correctAnswerKey;

}
