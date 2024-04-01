package api.educai.controllers;

import api.educai.adapters.UserAdapter;
import api.educai.dto.AuthDTO;
import api.educai.dto.LoginDTO;
import api.educai.dto.PatchUserEmailAndName;
import api.educai.dto.TokenDTO;
import api.educai.entities.User;
import api.educai.services.UserService;
import api.educai.utils.annotations.Authorized;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping
    public ResponseEntity<UserAdapter> createUser(@RequestBody @Valid User user) {
        return ResponseEntity.status(201).body(userService.createUser(user));
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthDTO> authUser(@RequestBody @Valid LoginDTO loginDTO, HttpServletResponse response) {
        AuthDTO authDTO = userService.autUser(loginDTO);

        Cookie cookie = new Cookie("refreshToken", authDTO.getRefreshToken());
        cookie.setMaxAge(15 * 24 * 60 * 60); // Expires in 15 days
        cookie.setSecure(true);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
        return ResponseEntity.status(200).body(authDTO);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<TokenDTO> refreshToken(@CookieValue(name = "refreshToken") @NotBlank String refreshToken) {
        return ResponseEntity.status(200).body(userService.renewUserToken(refreshToken));
    }

    @GetMapping
    public ResponseEntity<List<UserAdapter>> getUsers() {
        return ResponseEntity.status(200).body(userService.getUsers());
    }

    @PatchMapping
    @Authorized
    public ResponseEntity<UserAdapter> updateUserData(HttpServletRequest request, @RequestBody @Valid PatchUserEmailAndName patchUserEmailAndName) {
        ObjectId userId = (ObjectId) request.getAttribute("userId");

        return ResponseEntity.status(200).body(userService.updateUserData(userId, patchUserEmailAndName));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NotBlank String id) {
        userService.deleteUser(id);

        return ResponseEntity.status(200).build();
    }
}