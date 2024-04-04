package api.educai.controllers;

import api.educai.entities.Classwork;
import api.educai.services.ClassworkService;
import api.educai.utils.annotations.Authorized;
import api.educai.utils.annotations.Teacher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("classwork")
public class ClassworkController {
    @Autowired
    private ClassworkService classworkService;
    @PostMapping
    @Authorized
    @Teacher
    public ResponseEntity<Classwork> createClasswork(
            @RequestBody @Valid Classwork classwork,
            @RequestHeader ObjectId classroomId,
            HttpServletRequest request)
    {
        ObjectId userId = (ObjectId) request.getAttribute("userId");
        return status(201).body(classworkService.createClasswork(classwork, classroomId, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Classwork> getClassroomBy(@PathVariable ObjectId id) {
        Classwork classwork = classworkService.getClassworkById(id);

        return status(200).body(classwork);
    }

}
