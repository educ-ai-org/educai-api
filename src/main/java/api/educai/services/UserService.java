package api.educai.services;

import api.educai.dto.AuthDTO;
import api.educai.dto.LoginDTO;
import api.educai.dto.TokenDTO;
import api.educai.entities.User;
import api.educai.repositories.UserRespository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    @Autowired
    private UserRespository userRespository;

    public User createUser(User user) {
        if(emailAlreadyExists(user.getEmail())) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(409), "Email already registered!");
        }

        user.encryptPassword();

        return userRespository.save(user);
    }

    public AuthDTO autUser(LoginDTO loginDTO) {
        User user = userRespository.findByEmail(loginDTO.getEmail());

        if(user == null || !user.passwordIsValid(loginDTO.getPassword())) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(401), "E-mail or password are invalid!");
        }

        return new AuthDTO(TokenService.getToken(user), TokenService.getRefreshToken(user));
    }

    public TokenDTO renewUserToken(String refreshToken) {
        ObjectId userId = TokenService.getUserIdByToken(refreshToken);
        User user = userRespository.findById(userId);

        if(user == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Invalid Token");
        }

        return new TokenDTO(TokenService.getToken(user));
    }

    private boolean emailAlreadyExists(String email) {
        return userRespository.existsByEmail(email);
    }
}