package api.educai.controllers;

import api.educai.dto.UserDTO;
import api.educai.dto.ClassroomInfoDTO;
import api.educai.dto.AuthDTO;
import api.educai.dto.LoginDTO;
import api.educai.dto.PatchUserEmailAndName;
import api.educai.dto.TokenDTO;
import api.educai.entities.User;
import api.educai.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("user")
@Tag(name = "Usuarios", description = "API para serviços relacionados a usuarios.")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Cria um usuario")
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid User user) {
        return status(201).body(userService.createUser(user));
    }

    @Operation(summary = "Gera um token para o usuario")
    @PostMapping("/auth")
    public ResponseEntity<AuthDTO> authUser(@RequestBody @Valid LoginDTO loginDTO, HttpServletResponse response) {
        AuthDTO authDTO = userService.autUser(loginDTO);

        Cookie cookie = new Cookie("refreshToken", authDTO.getRefreshToken());
        cookie.setMaxAge(15 * 24 * 60 * 60); // Expires in 15 days
        cookie.setSecure(true);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
        return status(200).body(authDTO);
    }

    @Operation(summary = "Gera um refresh token para o usuario")
    @PostMapping("/refreshToken")
    public ResponseEntity<TokenDTO> refreshToken(@CookieValue(name = "refreshToken") @NotBlank String refreshToken) {
        return status(200).body(userService.renewUserToken(refreshToken));
    }

    @Operation(summary = "Exibe usuarios")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        return status(200).body(userService.getUsers());
    }

    @Operation(summary = "Atualiza email e nome de um usuario")
    @PatchMapping
    public ResponseEntity<UserDTO> updateUserData(HttpServletRequest request, @RequestBody @Valid PatchUserEmailAndName patchUserEmailAndName) {
        ObjectId userId = (ObjectId) request.getAttribute("userId");

        return status(200).body(userService.updateUserData(userId, patchUserEmailAndName));
    }

    @Operation(summary = "Deleta um usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NotBlank String id) {
        userService.deleteUser(id);

        return status(200).build();
    }

    @Operation(summary = "Exibe as salas de aula de um usuario")
    @GetMapping("/classrooms")
    public ResponseEntity<List<? extends ClassroomInfoDTO>> getUserClassrooms(HttpServletRequest request) {
        ObjectId userId = (ObjectId) request.getAttribute("userId");

        List<? extends ClassroomInfoDTO> classrooms = userService.getUserClassrooms(userId);

        if(classrooms.isEmpty()) {
            return status(204).build();
        }

        return status(200).body(classrooms);
    }

    @Operation(summary = "Realiza operação de logoff")
    @PostMapping("/logoff")
    public ResponseEntity<Void> logoff(
            HttpServletRequest request,
            @CookieValue(name = "refreshToken") @NotBlank String refreshToken,
            HttpServletResponse response
    ) {
        ObjectId userId = (ObjectId) request.getAttribute("userId");

        String authorization = request.getHeader("Authorization");
        String requestToken = authorization.replace("Bearer ", "");

        userService.logoff(userId, refreshToken, requestToken);

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return status(200).build();
    }
}
