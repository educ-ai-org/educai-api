package api.educai.services;

import api.educai.entities.*;
import api.educai.repositories.ClassroomRepository;
import api.educai.repositories.ClassworkRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClassworkService {
    @Autowired
    private ClassworkRepository classworkRepository;
    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private QuestionService questionService;

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

    public Classwork getClassworkById(ObjectId id) {
        Classwork classwork = classworkRepository.findById(id);

        if(classwork == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Invalid Token");
        }

        return classwork;
    }

}
