package api.educai.services;

import api.educai.dto.*;
import api.educai.entities.Classroom;
import api.educai.entities.Classwork;
import api.educai.entities.TokenBlacklist;
import api.educai.entities.User;
import api.educai.enums.Role;
import api.educai.repositories.TokenBlacklistRepository;
import api.educai.repositories.UserRespository;
import api.educai.utils.token.RefreshToken;
import api.educai.utils.token.Token;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRespository userRespository;
    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;
    @Autowired
    private Token token;
    @Autowired
    private RefreshToken refreshToken;

    public UserDTO createUser(User user) {
        validateUserEmail(user.getEmail());

        user.encodePassword();

        return new UserDTO(userRespository.save(user));
    }

    public List<UserDTO> getUsers() {
        List<User> users = userRespository.findAll();
        if (users.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204), "No users found!");
        }
        return users.stream().map(UserDTO::new).toList();
    }

    public AuthDTO autUser(LoginDTO loginDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        UserDetailsDTO user = (UserDetailsDTO) auth.getPrincipal();

        return new AuthDTO(token.getToken(user), refreshToken.getToken(user));
    }

    public TokenDTO renewUserToken(String userRefreshToken) {
        ObjectId userId = refreshToken.getUserIdByToken(userRefreshToken);

        if(refreshToken.isTokenBlacklisted(userId, userRefreshToken)) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(401), "Invalid refresh token");
        }

        User user = userRespository.findById(userId);

        if(user == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(401), "Invalid Token");
        }

        return new TokenDTO(token.getToken(new UserDetailsDTO(user)));
    }

    public void deleteUser(String id) {
        validateUserId(new ObjectId(id));

        userRespository.deleteById(new ObjectId(id));
    }

    public UserDTO updateUserData(ObjectId id, PatchUserEmailAndName patchUserEmailAndName) {
        validateUserId(id);

        User user = getUserById(id);
        if(!user.getEmail().equals(patchUserEmailAndName.getEmail())) {
            validateUserEmail(patchUserEmailAndName.getEmail());
        }

        userRespository.updateEmailAndName(id, patchUserEmailAndName.getName(), patchUserEmailAndName.getEmail());

        return new UserDTO(userRespository.findById(id));
    }

    public void addClassroom(ObjectId id, ObjectId classroomId) {
        validateUserId(id);

        userRespository.addClassroom(id, classroomId);
    }

    public List<? extends ClassroomInfoDTO> getUserClassrooms(ObjectId id) {
        User user = getUserById(id);

        if(user.getRole().equals(Role.TEACHER)) {
            return user.getClassrooms().stream().map(classroom -> {
                int classroomStudentsCount = (int) classroom.getParticipants().stream().filter(student -> student.getRole().equals(Role.STUDENT)).count();

                return new TeacherClassroomsDTO(classroom, classroomStudentsCount);
            }).toList();
        } else {
            return user.getClassrooms().stream().map(classroom -> {
                String nextSubmission = classroom.getClassworks().stream().min(Comparator.comparing(Classwork::getEndDate)).get().getTitle();

                return new StudentClassroomsDTO(classroom, nextSubmission);
            }).toList();
        }
    }

    public void logoff(ObjectId id, String refreshToken, String accessToken) {
        Optional<TokenBlacklist> tokenBlacklist = tokenBlacklistRepository.findById(id);

        if(tokenBlacklist.isEmpty()) {
            TokenBlacklist newTokenBlacklist = new TokenBlacklist(id);

            newTokenBlacklist.addAccessToken(accessToken, token.getTokenExpirationTime(accessToken));
            newTokenBlacklist.addRefreshToken(refreshToken, this.refreshToken.getTokenExpirationTime(refreshToken));

            tokenBlacklistRepository.save(newTokenBlacklist);
        } else {
            tokenBlacklist.get().addAccessToken(accessToken, token.getTokenExpirationTime(accessToken));
            tokenBlacklist.get().addRefreshToken(refreshToken, this.refreshToken.getTokenExpirationTime(refreshToken));

            tokenBlacklistRepository.save(tokenBlacklist.get());
        }
    }

    private void validateUserEmail(String email) {
        if(userEmailAlreadyExists(email)) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(409), "Email already registered!");
        }
    }

    public boolean userIdExists(ObjectId id) {
        return userRespository.existsById(id);
    }

    public boolean userEmailAlreadyExists(String email) {
        return userRespository.existsByEmail(email);
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

    public User getUserByEmail(String email) {
        return userRespository.findByEmail(email);
    }

    public void updateScore(Integer score, User user) {
        user.incrementScore(score);
        userRespository.save(user);
    }
}