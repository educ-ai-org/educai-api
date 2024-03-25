package api.educai.services;

import api.educai.entities.Classroom;
import api.educai.entities.User;
import api.educai.repositories.ClassroomRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassroomService {
    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private UserService userService;

    public Classroom createClassroom(Classroom classroom, ObjectId ownerId) {
        User user = userService.getUserById(ownerId);
        classroom.addParticipant(user);

        return classroomRepository.save(classroom);
    }
}
