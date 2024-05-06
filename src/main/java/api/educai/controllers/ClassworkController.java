package api.educai.controllers;

import api.educai.dto.AnswerDTO;
import api.educai.entities.Answer;
import api.educai.entities.Classwork;
import api.educai.entities.User;
import api.educai.services.ClassworkService;
import api.educai.services.UserService;
import api.educai.utils.annotations.Authorized;
import api.educai.utils.annotations.Teacher;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("classwork")
@Tag(name = "Atividades", description = "API para serviços relacionados a atividades.")
public class ClassworkController {
    @Autowired
    private ClassworkService classworkService;

    @PostMapping
    @Authorized
    @Teacher
    public ResponseEntity<Classwork> createClasswork(
            @RequestBody @Valid Classwork classwork,
            @RequestHeader ObjectId classroomId,
            HttpServletRequest request) {
        ObjectId userId = (ObjectId) request.getAttribute("userId");
        return status(201).body(classworkService.createClasswork(classwork, classroomId, userId));
    }

    @PostMapping("/answer")
    public ResponseEntity<Void> addAnswer(
            @RequestBody Answer answer,
            @RequestHeader ObjectId userId,
            @RequestHeader ObjectId classworkId
    ) {
        classworkService.addAnswer(answer, userId, classworkId);
        return status(201).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Classwork> getClassworkById(@PathVariable ObjectId id) {
        Classwork classwork = classworkService.getClassworkById(id);
        return status(200).body(classwork);
    }

    @GetMapping("/{id}/answers")
    @Authorized
    @Teacher
    public ResponseEntity<List<AnswerDTO>> getAnswers(@PathVariable ObjectId id) {
        List<AnswerDTO> answers = classworkService.getAnswers(id);
        return answers.isEmpty() ? status(204).build() : status(200).body(answers);
    }

}
