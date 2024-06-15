package api.educai.services;

import api.educai.dto.answer.AnswerDetailsDTO;
import api.educai.dto.classwork.ClassworkDetailsDTO;
import api.educai.dto.answer.QuestionAnswerDTO;
import api.educai.dto.answer.UserAnswerStatusDTO;
import api.educai.dto.user.UserDTO;
import api.educai.entities.*;
import api.educai.enums.Role;
import api.educai.repositories.AnswerRepository;
import api.educai.repositories.ClassroomRepository;
import api.educai.repositories.ClassworkRepository;
import api.educai.utils.FilaObj;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

        FilaObj<Question> questionsQueue = new FilaObj<>(classwork.getQuestions().size());
        for (Question question : classwork.getQuestions()) {
            questionsQueue.insert(question);
        }

        classwork.getQuestions().forEach(
                question -> {
                    addOptions(question.getOptions());
                }
        );
        addQuestions(questionsQueue);
        classworkRepository.save(classwork);

        classroom.addClasswork(classwork);
        classroomRepository.save(classroom);

        return classwork;
    }


    public void addQuestions(FilaObj<Question> questionsQueue) {
        if (questionsQueue.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "No questions to save");
        }

        List<Question> questions = new ArrayList<>();

        while (!questionsQueue.isEmpty()) {
            questions.add(questionsQueue.poll());
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
        answer.setCorrectPercentage(getAnswerScore(answer));
        answerRepository.save(answer);

        classwork.addAnswer(answer);
        classworkRepository.save(classwork);
    }

    public Classwork getClassworkById(ObjectId id) {
        Classwork classwork = classworkRepository.findById(id);

        if (classwork == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Classwork not found!");
        }

        return classwork;
    }

    public ClassworkDetailsDTO getClassworkDetailsById(ObjectId id) {
        Classwork classwork = classworkRepository.findById(id);

        if (classwork == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Classwork not found!");
        }

        return mapper.map(classwork, ClassworkDetailsDTO.class);
    }

    public AnswerDetailsDTO getAnswer(ObjectId classworkId, ObjectId userId) {

        Answer answer = answerRepository.findByUserIdAndClassworkId(userId, classworkId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "Answer not found!"));

        return mapper.map(answer, AnswerDetailsDTO.class);
    }

    public double getAnswerScore(Answer answer) {

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

        return (int) ((userCorrectAnswers.size() * 100.0) / questions.size());

    }

    public List<UserAnswerStatusDTO> getUserAnswerStatus(ObjectId classworkId) {

        Classroom classroom = classroomService.getClassroomByClassworkId(classworkId);
        List<User> classroomUsers = classroom.getParticipants();

        return classroomUsers.stream()
                .filter(user -> user.getRole().equals(Role.STUDENT))
                .map(user -> {
                    UserAnswerStatusDTO userAnswerStatus = new UserAnswerStatusDTO();
                    userAnswerStatus.setUser(mapper.map(user, UserDTO.class));
                    Optional<Answer> answer = answerRepository.findByUserIdAndClassworkId(user.getId(), classworkId);
                    if (answer.isEmpty()) {
                        userAnswerStatus.setHasAnswered(false);
                    } else {
                        userAnswerStatus.setHasAnswered(true);
                        userAnswerStatus.setCorrectPercentage(answer.get().getCorrectPercentage());
                    }
                    return userAnswerStatus;
                }).toList();
    }

    public Classwork deleteClassworkById(ObjectId id) {
        Classwork classwork = classworkRepository.findById(id);

        if (classwork == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Classwork not found!");
        }

        return classworkRepository.deleteById(id);
    }

}
