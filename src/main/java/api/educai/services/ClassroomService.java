package api.educai.services;

import api.educai.adapters.ClassroomDataAdapter;
import api.educai.adapters.ClassroomInfoAdapter;
import api.educai.dto.ClassroomParticipantsDTO;
import api.educai.entities.Classroom;
import api.educai.entities.User;
import api.educai.repositories.ClassroomRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClassroomService {
    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private UserService userService;

    public ClassroomInfoAdapter createClassroom(Classroom classroom, ObjectId ownerId) {
        User user = userService.getUserById(ownerId);

        classroom.addParticipant(user);
        classroomRepository.save(classroom);
        userService.addClassroom(user.getId(), classroom.getId());

        return new ClassroomInfoAdapter(classroom);
    }

    public ClassroomDataAdapter getClassroomDataById(ObjectId id) {
        Classroom classroom = getClassroomById(id);
        List<ClassroomParticipantsDTO> participants = classroom.getParticipants().stream().map(ClassroomParticipantsDTO::new).toList();

        return new ClassroomDataAdapter(classroom, participants);
    }

    public Classroom getClassroomById(ObjectId id) {
        Classroom classroom = classroomRepository.findById(id);

        if(classroom == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Invalid Token");
        }

        return classroom;
    }
}
