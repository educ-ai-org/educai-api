package api.educai.controllers;

import api.educai.adapters.ClassroomDataAdapter;
import api.educai.adapters.ClassroomInfoAdapter;
import api.educai.entities.Classroom;
import api.educai.services.ClassroomService;
import api.educai.utils.annotations.Authorized;
import api.educai.utils.annotations.Teacher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("classroom")
public class ClassroomController {
    @Autowired
    private ClassroomService classroomService;
    @PostMapping
    @Authorized
    @Teacher
    public ResponseEntity<ClassroomInfoAdapter> createClassroom(@RequestBody @Valid Classroom classroom, HttpServletRequest request) {
        ObjectId userId = (ObjectId) request.getAttribute("userId");

        return status(201).body(classroomService.createClassroom(classroom, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomDataAdapter> getClassroomBy(@PathVariable ObjectId id) {
        ClassroomDataAdapter users = classroomService.getClassroomDataById(id);

        return status(200).body(users);
    }
}
