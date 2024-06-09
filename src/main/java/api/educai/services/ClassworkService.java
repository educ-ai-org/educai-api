package api.educai.services;

import api.educai.dto.answer.AnswerDTO;
import api.educai.dto.classwork.ClassworkDetailsDTO;
import api.educai.dto.answer.QuestionAnswerDTO;
import api.educai.dto.answer.UserAnswerStatusDTO;
import api.educai.dto.user.UserDTO;
import api.educai.entities.*;
import api.educai.enums.Role;
import api.educai.repositories.AnswerRepository;
import api.educai.repositories.ClassroomRepository;
import api.educai.repositories.ClassworkRepository;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    private ModelMapper mapper;

    private final Integer CLASSWORK_SCORE = 10;

    public Classwork createClasswork(Classwork classwork, ObjectId classroomId, ObjectId userId) {

        Classroom classroom = classroomService.getClassroomById(classroomId);

        classwork.getQuestions().forEach(
                question -> {
                    addOptions(question.getOptions());
                }
        );
        addQuestions(classwork.getQuestions());
        classworkRepository.save(classwork);

        classroom.addClasswork(classwork);
        classroomRepository.save(classroom);

        return classwork;
    }


    public void addQuestions(List<Question> questions) {
        if (questions.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "No questions to save");
        }
        questionService.addQuestions(questions);
    }

    public void addOptions(List<Option> options) {
        if (options.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "No options to save");
        }
        questionService.addOptions(options);
    }

    public void addAnswer(Answer answer, ObjectId userId, ObjectId classworkId) {
        Classwork classwork = getClassworkById(classworkId);

        if (answerRepository.existsAnswerByUserIdAndClassworkId(userId, classworkId)) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(409), "Answer already exists!");
        }

        answer.setUser(userService.getUserById(userId));
        answer.setClasswork(classwork);
        answer.setCorrectPercentage(getAnswerScore(answer) * 10.0);
        answerRepository.save(answer);

        classwork.addAnswer(answer);
        classworkRepository.save(classwork);
    }

    public Classwork getClassworkById(ObjectId id) {
        Classwork classwork = classworkRepository.findById(id);

        if(classwork == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Classwork not found!");
        }

        return classwork;
    }

    public ClassworkDetailsDTO getClassworkDetailsById(ObjectId id) {
        Classwork classwork = classworkRepository.findById(id);

        if(classwork == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Classwork not found!");
        }

        return mapper.map(classwork, ClassworkDetailsDTO.class);
    }

    public AnswerDTO getAnswers(ObjectId classworkId, ObjectId userId) {
        Classwork classwork = classworkRepository.findById(classworkId);
        return classwork.getAnswers().stream()
                .filter(answer -> answer.getUser().getId().equals(userId))
                .findFirst()
                .map(AnswerDTO::new)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "Answer not found!"));
    }

    public int getAnswerScore(Answer answer) {

        List<Question> questions = answer.getClasswork().getQuestions();
        List<QuestionAnswerDTO> answers = answer.getQuestionAnswers();

        Map<ObjectId, String> correctAnswersMap = questions.stream()
                .collect(Collectors.toMap(Question::getId, Question::getCorrectAnswerKey));

        Map<ObjectId, String> userCorrectAnswers = answers.stream()
                .filter(questionAnswer -> correctAnswersMap.containsKey(questionAnswer.getQuestionId()) &&
                        correctAnswersMap.get(questionAnswer.getQuestionId()).equals(questionAnswer.getOptionKey()))
                .collect(Collectors.toMap(QuestionAnswerDTO::getQuestionId, QuestionAnswerDTO::getOptionKey));

        int score = userCorrectAnswers.size() * CLASSWORK_SCORE / questions.size();
        userService.updateScore(score, answer.getUser());

        return score;

    }

    public List<UserAnswerStatusDTO> getUserAnswerStatus(ObjectId classworkId) {

        Classroom classroom = classroomService.getClassroomByClassworkId(classworkId);
        List<User> classroomUsers = classroom.getParticipants();

        return classroomUsers.stream()
            .filter(user -> user.getRole().equals(Role.STUDENT))
            .map(user -> {
                UserAnswerStatusDTO userAnswerStatus = new UserAnswerStatusDTO();
                userAnswerStatus.setUser(mapper.map(user, UserDTO.class));
                boolean hasAnswered = answerRepository.existsAnswerByUserIdAndClassworkId(user.getId(), classworkId);
                userAnswerStatus.setHasAnswered(hasAnswered);
                return userAnswerStatus;
            }).toList();
    }

}
