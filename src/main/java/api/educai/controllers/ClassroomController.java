package api.educai.controllers;

import api.educai.adapters.ClassroomInfoAdapter;
import api.educai.adapters.UserAdapter;
import api.educai.dto.ClassworkDTO;
import api.educai.entities.Classroom;
import api.educai.entities.Classwork;
import api.educai.services.ClassroomService;
import api.educai.utils.annotations.Authorized;
import api.educai.utils.annotations.Teacher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("classroom")
public class ClassroomController {
    @Autowired
    private ClassroomService classroomService;
    @PostMapping
    @Secured("ROLE_TEACHER")
    public ResponseEntity<ClassroomInfoAdapter> createClassroom(@RequestBody @Valid Classroom classroom, HttpServletRequest request) {
        ObjectId userId = (ObjectId) request.getAttribute("userId");

        return status(201).body(classroomService.createClassroom(classroom, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomInfoAdapter> getClassroomBy(@PathVariable ObjectId id) {
        ClassroomInfoAdapter users = classroomService.getClassroomDataById(id);

        return status(200).body(users);
    }

    @PostMapping("/{id}/invite")
    @Secured("ROLE_TEACHER")
    public ResponseEntity<Void> inviteUser(@PathVariable ObjectId id, @RequestBody @Valid UserAdapter user) {
        classroomService.inviteUser(id, user);

        return status(201).build();
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<UserAdapter>> getClassroomParticipants(@PathVariable ObjectId id) {
        return status(200).body(classroomService.getClassroomParticipants(id));
    }

    @GetMapping("/{id}/classworks")
    public ResponseEntity<List<ClassworkDTO>> getClassworksByClassroom(@PathVariable ObjectId id) {
        List<ClassworkDTO> classworks = classroomService.getClassworks(id);
        return classworks.isEmpty() ? status(204).build() : status(200).body(classworks);
    }

}
