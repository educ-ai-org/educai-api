package api.educai.services;

import api.educai.dto.AuthDTO;
import api.educai.dto.LoginDTO;
import api.educai.dto.PatchUserEmailAndName;
import api.educai.dto.TokenDTO;
import api.educai.entities.User;
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

    public User createUser(User user) {
        if(emailAlreadyExists(user.getEmail())) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(409), "Email already registered!");
        }

        user.encryptPassword();

        return userRespository.save(user);
    }

    public List<User> getUsers() {
        List<User> users = userRespository.findAll();
        if (users.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204), "No users found!");
        }
        return users;
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
        if(userIdExists(id)) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "User not found!");
        }

        userRespository.deleteById(new ObjectId(id));
    }

    public User updateUserData(ObjectId id, PatchUserEmailAndName patchUserEmailAndName) {
        validateUserId(id);

        userRespository.updateEmailAndName(id, patchUserEmailAndName.getName(), patchUserEmailAndName.getEmail());

        return userRespository.findById(id);
    }

    private boolean emailAlreadyExists(String email) {
        return userRespository.existsByEmail(email);
    }

    public boolean userIdExists(String id) {
        ObjectId userId = new ObjectId(id);

        return userRespository.existsById(userId);
    }

    public boolean userIdExists(ObjectId id) {
        return userRespository.existsById(id);
    }

    public void validateUserId(String id) {
        if(!userIdExists(id)) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "User not found");
        }
    }

    public void validateUserId(ObjectId id) {
        if(!userIdExists(id)) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "User not found");
        }
    }
}