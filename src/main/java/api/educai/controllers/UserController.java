package api.educai.controllers;

import api.educai.dto.AuthDTO;
import api.educai.dto.LoginDTO;
import api.educai.entities.User;
import api.educai.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        try {
            return ResponseEntity.status(201).body(userService.createUser(user));
        } catch (Error error) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(409), error.getMessage());
        }
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthDTO> authUser(@RequestBody @Valid LoginDTO loginDTO, HttpServletResponse response) {
        try {
            AuthDTO authDTO = userService.autUser(loginDTO);

            Cookie cookie = new Cookie("refreshToken", authDTO.getRefreshToken());
            cookie.setMaxAge(15 * 24 * 60 * 60); // Expires in 15 days
            cookie.setSecure(true);
            cookie.setHttpOnly(true);

            response.addCookie(cookie);
            return ResponseEntity.status(200).body(authDTO);
        } catch (Error error) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(401), error.getMessage());
        }
    }
}