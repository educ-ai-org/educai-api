package api.educai.services;

import api.educai.adapters.ClassroomInfoAdapter;
import api.educai.adapters.UserAdapter;
import api.educai.dto.AddStudentInClassroomDTO;
import api.educai.dto.NewStudentEmailDTO;
import api.educai.dto.ClassroomParticipantsDTO;
import api.educai.dto.ClassworkDTO;
import api.educai.entities.Classroom;
import api.educai.entities.Classwork;
import api.educai.entities.User;
import api.educai.repositories.ClassroomRepository;
import api.educai.utils.PasswordGenerator;
import api.educai.utils.email.EmailService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@Service
public class ClassroomService {
    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private UserService userService;

    public ClassroomInfoAdapter createClassroom(Classroom classroom, ObjectId ownerId) {
        User user = userService.getUserById(ownerId);

        addUserInClassroom(classroom, user);

        return new ClassroomInfoAdapter(classroom);
    }

    public ClassroomInfoAdapter getClassroomDataById(ObjectId id) {
        Classroom classroom = getClassroomById(id);

        return new ClassroomInfoAdapter(classroom);
    }

    public void inviteUser(ObjectId id, UserAdapter newUser) {
        if(!userService.userEmailAlreadyExists(newUser.getEmail())) {
            String userPassword = PasswordGenerator.generate(8);
            User user = new User(newUser.getName(), newUser.getEmail(), userPassword, newUser.getRole());
            Classroom classroom = getClassroomById(id);

            userService.createUser(user);
            addUserInClassroom(classroom, user);

            NewStudentEmailDTO newStudentEmailDTO = new NewStudentEmailDTO(
                    user.getEmail(),
                    userPassword,
                    classroom.getCourse() + " - " + classroom.getTitle()
            );

            try {
                EmailService.sendEmailSuccessfulCreateAccount(newStudentEmailDTO);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            User user = userService.getUserByEmail(newUser.getEmail());
            Classroom classroom = getClassroomById(id);

            if(user.isUserEnrolledInClassroom(id)) {
                throw new ResponseStatusException(HttpStatusCode.valueOf(409), "This user is already registered in this classroom");
            }

            addUserInClassroom(classroom, user);

            AddStudentInClassroomDTO addStudentInClassroomDTO = new AddStudentInClassroomDTO(
                    user.getEmail(),
                    classroom.getCourse() + " - " + classroom.getTitle()
            );

            try {
                EmailService.sendEmailAddUserInClassroom(addStudentInClassroomDTO);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<UserAdapter> getClassroomParticipants(ObjectId id) {
        Classroom classroom = getClassroomById(id);

        return classroom.getParticipants().stream().map(UserAdapter::new).toList();
    }

    public Classroom getClassroomById(ObjectId id) {
        Classroom classroom = classroomRepository.findById(id);

        if(classroom == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Classroom not found!");
        }

        return classroom;
    }

    private void addUserInClassroom(Classroom classroom, User user) {
        classroom.addParticipant(user);
        classroomRepository.save(classroom);
        userService.addClassroom(user.getId(), classroom.getId());
    }
    public List<ClassworkDTO> getClassworks(ObjectId id) {
        Classroom classroom = getClassroomById(id);
        return classroom.getClassworks().stream().map(ClassworkDTO::new).toList();
    }

}
