package api.educai.controllers;

import api.educai.dto.AnswerDTO;
import api.educai.dto.ClassworkDetailsDTO;
import api.educai.entities.Answer;
import api.educai.entities.Classwork;
import api.educai.services.ClassworkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("classwork")
@Tag(name = "Atividades", description = "API para serviços relacionados a atividades.")
public class ClassworkController {
    @Autowired
    private ClassworkService classworkService;

    @Operation(summary = "Cria uma sala de aula")
    @Secured("ROLE_TEACHER")
    @PostMapping
    public ResponseEntity<Classwork> createClasswork(
            @RequestBody @Valid Classwork classwork,
            @RequestHeader ObjectId classroomId,
            HttpServletRequest request) {
        ObjectId userId = (ObjectId) request.getAttribute("userId");
        return status(201).body(classworkService.createClasswork(classwork, classroomId, userId));
    }

    @Operation(summary = "Cria uma resposta para uma atividade")
    @Secured("ROLE_STUDENT")
    @PostMapping("/answer")
    public ResponseEntity<Void> addAnswer(
            @RequestBody @Valid Answer answer,
            @RequestHeader ObjectId userId,
            @RequestHeader ObjectId classworkId
    ) {
        classworkService.addAnswer(answer, userId, classworkId);
        return status(201).build();
    }

    @Operation(summary = "Retorna uma atividade via id")
    @GetMapping("/{id}")
    public ResponseEntity<ClassworkDetailsDTO> getClassworkById(@PathVariable ObjectId id) {
        ClassworkDetailsDTO  classwork = classworkService.getClassworkDetailsById(id);
        return status(200).body(classwork);
    }

    @Operation(summary = "Retorna respostas de uma atividade")
    @Secured("ROLE_TEACHER")
    @GetMapping("/{id}/answers")
    public ResponseEntity<List<AnswerDTO>> getAnswers(@PathVariable ObjectId id) {
        List<AnswerDTO> answers = classworkService.getAnswers(id);
        return answers.isEmpty() ? status(204).build() : status(200).body(answers);
    }

}
