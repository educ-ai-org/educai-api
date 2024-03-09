package api.educai.services;

import api.educai.dto.AuthDTO;
import api.educai.dto.LoginDTO;
import api.educai.entities.User;
import api.educai.repositories.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRespository userRespository;

    public User createUser(User user) {
        if(emailAlreadyExists(user.getEmail())) {
            throw new Error("Email already registered!");
        }

        user.encryptPassword();

        return userRespository.save(user);
    }

    public AuthDTO autUser(LoginDTO loginDTO) {
        User user = userRespository.findByEmail(loginDTO.getEmail());

        if(user == null || !user.passwordIsValid(loginDTO.getPassword())) {
            throw new Error("E-mail or password are invalid!");
        }

        return new AuthDTO(TokenService.getToken(user), TokenService.getRefreshToken());
    }

    private boolean emailAlreadyExists(String email) {
        return userRespository.existsByEmail(email);
    }
}