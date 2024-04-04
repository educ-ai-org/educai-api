package api.educai.services;

import api.educai.entities.Option;
import api.educai.entities.Question;
import api.educai.repositories.OptionRepository;
import api.educai.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private OptionRepository optionRepository;

    public void addQuestions(List<Question> questions) {
        questionRepository.saveAll(questions);
    }

    public void addOptions(List<Option> options) {
        optionRepository.saveAll(options);
    }

}
