package api.educai.services;

import api.educai.adapters.StudentClassroomsAdapter;
import api.educai.adapters.TeacherClassroomsAdapter;
import api.educai.adapters.UserAdapter;
import api.educai.adapters.ClassroomInfoAdapter;
import api.educai.dto.AuthDTO;
import api.educai.dto.LoginDTO;
import api.educai.dto.PatchUserEmailAndName;
import api.educai.dto.TokenDTO;
import api.educai.entities.User;
import api.educai.enums.Role;
import api.educai.repositories.UserRespository;
import api.educai.utils.token.RefreshToken;
import api.educai.utils.token.Token;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRespository userRespository;
    private Token token = new Token();
    private RefreshToken refreshToken = new RefreshToken();

    public UserAdapter createUser(User user) {
        validateUserEmail(user.getEmail());

        user.encryptPassword();

        return new UserAdapter(userRespository.save(user));
    }

    public List<UserAdapter> getUsers() {
        List<User> users = userRespository.findAll();
        if (users.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204), "No users found!");
        }
        return users.stream().map(UserAdapter::new).toList();
    }

    public AuthDTO autUser(LoginDTO loginDTO) {
        User user = userRespository.findByEmail(loginDTO.getEmail());

        if(user == null || !user.passwordIsValid(loginDTO.getPassword())) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(401), "E-mail or password are invalid!");
        }

        return new AuthDTO(token.getToken(user), refreshToken.getToken(user));
    }

    public TokenDTO renewUserToken(String userRefreshToken) {
        ObjectId userId = refreshToken.getUserIdByToken(userRefreshToken);
        User user = userRespository.findById(userId);

        if(user == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Invalid Token");
        }

        return new TokenDTO(token.getToken(user));
    }

    public void deleteUser(String id) {
        validateUserId(new ObjectId(id));

        userRespository.deleteById(new ObjectId(id));
    }

    public UserAdapter updateUserData(ObjectId id, PatchUserEmailAndName patchUserEmailAndName) {
        validateUserId(id);

        User user = getUserById(id);
        if(!user.getEmail().equals(patchUserEmailAndName.getEmail())) {
            validateUserEmail(patchUserEmailAndName.getEmail());
        }

        userRespository.updateEmailAndName(id, patchUserEmailAndName.getName(), patchUserEmailAndName.getEmail());

        return new UserAdapter(userRespository.findById(id));
    }

    public void addClassroom(ObjectId id, ObjectId classroomId) {
        validateUserId(id);

        userRespository.addClassroom(id, classroomId);
    }

    public List<? extends ClassroomInfoAdapter> getUserClassrooms(ObjectId id) {
        User user = getUserById(id);

        if(user.getRole().equals(Role.TEACHER)) {
            return user.getClassrooms().stream().map(classroom -> {
                int classroomStudentsCount = (int) classroom.getParticipants().stream().filter(student -> student.getRole().equals(Role.STUDENT)).count();

                return new TeacherClassroomsAdapter(classroom, classroomStudentsCount);
            }).toList();
        } else {
            //TODO -> Retornar as atividades pendentes caso seja um estudante
            return user.getClassrooms().stream().map(classroom -> new StudentClassroomsAdapter(classroom, 3)).toList();
        }
    }

    private void validateUserEmail(String email) {
        if(userRespository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(409), "Email already registered!");
        }
    }

    public boolean userIdExists(ObjectId id) {
        return userRespository.existsById(id);
    }

    public void validateUserId(ObjectId id) {
        if(!userIdExists(id)) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "User not found");
        }
    }

    public User getUserById(ObjectId id) {
        User user = userRespository.findById(id);

        if(user == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Invalid Token");
        }

        return user;
    }
}