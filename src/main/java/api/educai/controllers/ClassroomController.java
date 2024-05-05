package api.educai.controllers;

import api.educai.dto.ClassroomInfoDTO;
import api.educai.dto.ReportDTO;
import api.educai.dto.UserDTO;
import api.educai.dto.ClassworkDTO;
import api.educai.entities.Classroom;
import api.educai.entities.Post;
import api.educai.services.ClassroomService;
import api.educai.services.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("classroom")
public class ClassroomController {
    @Autowired
    private ClassroomService classroomService;
    @PostMapping
    @Secured("ROLE_TEACHER")
    public ResponseEntity<ClassroomInfoDTO> createClassroom(@RequestBody @Valid Classroom classroom, HttpServletRequest request) {
        ObjectId userId = (ObjectId) request.getAttribute("userId");

        return status(201).body(classroomService.createClassroom(classroom, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomInfoDTO> getClassroomBy(@PathVariable ObjectId id) {
        ClassroomInfoDTO users = classroomService.getClassroomDataById(id);

        return status(200).body(users);
    }

    @PostMapping("/{id}/invite")
    @Secured("ROLE_TEACHER")
    public ResponseEntity<Void> inviteUser(@PathVariable ObjectId id, @RequestBody @Valid UserDTO user) {
        classroomService.inviteUser(id, user);

        return status(201).build();
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<UserDTO>> getClassroomParticipants(@PathVariable ObjectId id) {
        return status(200).body(classroomService.getClassroomParticipants(id));
    }

    @GetMapping("/{id}/classworks")
    public ResponseEntity<List<ClassworkDTO>> getClassworksByClassroom(@PathVariable ObjectId id) {
        List<ClassworkDTO> classworks = classroomService.getClassworks(id);
        return classworks.isEmpty() ? status(204).build() : status(200).body(classworks);
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<List<Post>> getPostsByClassroom(@PathVariable ObjectId id){
        List<Post> posts = classroomService.getPostsByClassroom(id);
        return posts.isEmpty() ? status(204).build() : status(200).body(posts);
    }

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

}
