package api.educai.middlewares;

import api.educai.enums.Role;
import api.educai.repositories.UserRespository;
import api.educai.services.UserService;
import api.educai.utils.annotations.Authorized;
import api.educai.utils.token.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Autowired
    private Token token;
    @Autowired
    private UserRespository userRespository;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if (!handlerMethod.hasMethodAnnotation(Authorized.class)) {
            return true;
        }

        String authorization = request.getHeader("Authorization");

        if (authorization == null || authorization.isBlank()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(401), "Token is not present in header 'Authorization'.");
        }

        String requestToken = authorization.replace("Bearer ", "");

        ObjectId userId = token.getUserIdByToken(requestToken);

        if(token.isTokenBlacklisted(userId, requestToken)) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(401), "Invalid Token");
        }

        Role userRole = token.getUserRoleByToken(requestToken);

        request.setAttribute("userId", userId);
        request.setAttribute("userRole", userRole);

        return true;
    }
}
