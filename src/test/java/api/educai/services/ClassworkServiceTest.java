package api.educai.services;

import api.educai.entities.*;
import api.educai.repositories.AnswerRepository;
import api.educai.repositories.ClassroomRepository;
import api.educai.repositories.ClassworkRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClassworkServiceTest {

    @InjectMocks
    private ClassworkService classworkService;

    @Mock
    private ClassworkRepository classworkRepository;

    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private ClassroomService classroomService;

    @Mock
    private QuestionService questionService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addQuestions_EmptyList() {
        assertThrows(ResponseStatusException.class, () -> classworkService.addQuestions(Collections.emptyList()));
    }

    @Test
    public void addOptions_EmptyList() {
        assertThrows(ResponseStatusException.class, () -> classworkService.addOptions(Collections.emptyList()));
    }

    @Test
    public void getClassworkById_NotFound() {
        ObjectId id = new ObjectId();
        when(classworkRepository.findById(id)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> classworkService.getClassworkById(id));
    }

//    @Test
//    public void getAnswerScore_HappyPath() {
//        Classwork classwork = new Classwork();
//        classwork.setQuestions(Arrays.asList(new Question()));
//        Answer answer = new Answer();
//        answer.setClasswork(classwork);
//        answer.setQuestionAnswers(Arrays.asList(new QuestionAnswerDTO()));
//
//        int score = classworkService.getAnswerScore(answer);
//
//        assertEquals(0, score);
//        verify(userService, times(1)).updateScore(anyInt(), any(User.class));
//    }

}