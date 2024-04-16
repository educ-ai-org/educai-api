package api.educai.services;

import api.educai.dto.AnswerDTO;
import api.educai.dto.QuestionAnswerDTO;
import api.educai.entities.*;
import api.educai.repositories.AnswerRepository;
import api.educai.repositories.ClassroomRepository;
import api.educai.repositories.ClassworkRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClassworkService {
    @Autowired
    private ClassworkRepository classworkRepository;
    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;

    private final Integer CLASSWORK_SCORE = 10;

    public Classwork createClasswork(Classwork classwork, ObjectId classroomId, ObjectId userId) {

        Classroom classroom = classroomService.getClassroomById(classroomId);

        addOptions(classwork.getQuestions().stream().map(Question::getOptions).toList().get(0));
        addQuestions(classwork.getQuestions());
        classworkRepository.save(classwork);

        classroom.addClasswork(classwork);
        classroomRepository.save(classroom);

        return classwork;
    }


    public void addQuestions(List<Question> questions) {
        questionService.addQuestions(questions);
    }

    public void addOptions(List<Option> options) {
        questionService.addOptions(options);
    }

    public void addAnswer(Answer answer, ObjectId userId, ObjectId classworkId) {
        Classwork classwork = getClassworkById(classworkId);

        answer.setUser(userService.getUserById(userId));
        answer.setClasswork(classwork);
        correctAnswer(answer);
        answerRepository.save(answer);

        classwork.addAnswer(answer);
        classworkRepository.save(classwork);
    }

    public Classwork getClassworkById(ObjectId id) {
        Classwork classwork = classworkRepository.findById(id);

        if(classwork == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Invalid Token");
        }

        return classwork;
    }

    public List<AnswerDTO> getAnswers(ObjectId classworkId) {
        Classwork classwork = classworkRepository.findById(classworkId);
        return classwork.getAnswers().stream().map(AnswerDTO::new).toList();
    }

    public void correctAnswer(Answer answer) {

        List<Question> questions = answer.getClasswork().getQuestions();
        List<QuestionAnswerDTO> answers = answer.getQuestionAnswers();

        Map<ObjectId, String> correctAnswersMap = questions.stream()
                .collect(Collectors.toMap(Question::getId, Question::getCorrectAnswerKey));

        Map<ObjectId, String> userCorrectAnswers = answers.stream()
                .filter(questionAnswer -> correctAnswersMap.containsKey(questionAnswer.getQuestionId()) &&
                        correctAnswersMap.get(questionAnswer.getQuestionId()).equals(questionAnswer.getOptionKey()))
                .collect(Collectors.toMap(QuestionAnswerDTO::getQuestionId, QuestionAnswerDTO::getOptionKey));

        int score = userCorrectAnswers.size() * CLASSWORK_SCORE / questions.size();
        answer.setCorrectPercentage(score * 10.0);
        answer.getUser().incrementScore(score);

    }

}
