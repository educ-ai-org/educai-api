package api.educai.controllers;

import api.educai.entities.Classroom;
import api.educai.entities.User;
import api.educai.services.ClassroomService;
import api.educai.services.UserService;
import api.educai.utils.annotations.Authorized;
import api.educai.utils.annotations.Teacher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("classroom")
public class ClassroomController {
    @Autowired
    private ClassroomService classroomService;
    @PostMapping
    @Authorized
    @Teacher
    public ResponseEntity<Classroom> createClassroom(@RequestBody @Valid Classroom classroom, HttpServletRequest request) {
        ObjectId userId = (ObjectId) request.getAttribute("userId");

        return status(201).body(classroomService.createClassroom(classroom, userId));
    }
}
