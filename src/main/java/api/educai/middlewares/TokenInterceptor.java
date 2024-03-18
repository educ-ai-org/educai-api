package api.educai.middlewares;

import api.educai.services.UserService;
import api.educai.utils.annotations.Authorized;
import api.educai.utils.token.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

@Aspect
@Component
public class TokenInterceptor implements HandlerInterceptor {
    private Token token = new Token();
    @Autowired
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof Method) {
            Method method = (Method) handler;

            if (method.isAnnotationPresent(Authorized.class)) {
                String authorization = request.getHeader("Authorization");

                if (authorization.isBlank()) {
                    response.setStatus(409);
                    response.getWriter().write("Token is not present in header 'Authorization'.");
                    return false;
                }

                ObjectId userId = token.getUserIdByToken(request.getHeader("Authorization"));

                if(userService.userIdExists(request.getHeader("Authorization"))) {
                    response.setStatus(409);
                    response.getWriter().write("Invalid token");
                    return false;
                }

                request.setAttribute("userId", userId);
            }
        }

        return true;
    }
}
