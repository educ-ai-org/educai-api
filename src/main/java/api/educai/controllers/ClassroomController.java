package api.educai.controllers;

import api.educai.dto.*;
import api.educai.entities.Classroom;
import api.educai.entities.Post;
import api.educai.services.ClassroomService;
import api.educai.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("classroom")
@Tag(name = "Sala de aula", description = "API para serviços relacionados a salas de aula.")
public class ClassroomController {
    @Autowired
    private ClassroomService classroomService;
    @Operation(summary = "Cria uma sala de aula")
    @PostMapping
    @Secured("ROLE_TEACHER")
    public ResponseEntity<ClassroomInfoDTO> createClassroom(@RequestBody @Valid Classroom classroom, HttpServletRequest request) {
        ObjectId userId = (ObjectId) request.getAttribute("userId");

        return status(201).body(classroomService.createClassroom(classroom, userId));
    }

    @Operation(summary = "Retorna uma sala de aula especifica")
    @GetMapping("/{id}")
    public ResponseEntity<ClassroomInfoDTO> getClassroomBy(@PathVariable ObjectId id) {
        ClassroomInfoDTO users = classroomService.getClassroomDataById(id);

        return status(200).body(users);
    }

    @Operation(summary = "Convida um usuario para a sala de aula")
    @PostMapping("/{id}/invite")
    @Secured("ROLE_TEACHER")
    public ResponseEntity<Void> inviteUser(@PathVariable ObjectId id, @RequestBody @Valid UserDTO user) {
        classroomService.inviteUser(id, user);

        return status(201).build();
    }

    @Operation(summary = "Retorna os participantes de uma sala de aula especifica")
    @GetMapping("/{id}/participants")
    public ResponseEntity<List<UserDTO>> getClassroomParticipants(@PathVariable ObjectId id) {
        return status(200).body(classroomService.getClassroomParticipants(id));
    }

    @Operation(summary = "Retorna atividades de uma sala de aula")
    @GetMapping("/{id}/classworks")
    public ResponseEntity<List<ClassworkDTO>> getClassworksByClassroom(@PathVariable ObjectId id) {
        List<ClassworkDTO> classworks = classroomService.getClassworks(id);
        return classworks.isEmpty() ? status(204).build() : status(200).body(classworks);
    }

    @Operation(summary = "Retorna posts de uma sala de aula")
    @GetMapping("/{id}/posts")
    public ResponseEntity<List<Post>> getPostsByClassroom(@PathVariable ObjectId id){
        List<Post> posts = classroomService.getPostsByClassroom(id);
        return posts.isEmpty() ? status(204).build() : status(200).body(posts);
    }

    @Operation(summary = "Retorna um csv com resultados de uma atividade")
    @GetMapping(value = "/{classroomId}/report", produces = "text/csv")
    public ResponseEntity<byte[]> getUserReport(@PathVariable ObjectId classroomId, @RequestHeader ObjectId userId) {
        ReportDTO report = classroomService.getUserReport(classroomId, userId);
        byte[] reportBytes = report.getCsv();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", report.getFileName());

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(reportBytes);
    }

    @Operation(summary = "Deleta uma sala de aula")
    @DeleteMapping("/{id}")
    @Secured("ROLE_TEACHER")
    public ResponseEntity<Void> deleteClassroom(@PathVariable ObjectId id, HttpServletRequest request) {
        ObjectId userId = (ObjectId) request.getAttribute("userId");
        classroomService.deleteClassroom(id, userId);

        return status(204).build();
    }

    @Operation(summary = "Atualiza titulo e curso de uma sala de aula")
    @PatchMapping("/{id}")
    @Secured("ROLE_TEACHER")
    public ResponseEntity<ClassroomInfoDTO> updateClassroom(@PathVariable ObjectId id, @RequestBody @Valid PatchClassroomTitleAndCourse classroom, HttpServletRequest request) {
        ObjectId userId = (ObjectId) request.getAttribute("userId");

        if (classroom == null || (classroom.getTitle() == null && classroom.getCourse() == null)) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Title and Course cannot both be null");
        }

        return status(200).body(classroomService.updateClassroom(id, classroom, userId));
    }
}
