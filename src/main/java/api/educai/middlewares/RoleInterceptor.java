package api.educai.middlewares;

import api.educai.enums.Role;
import api.educai.utils.annotations.Student;
import api.educai.utils.annotations.Teacher;
import api.educai.utils.token.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

public class RoleInterceptor implements HandlerInterceptor {
    @Autowired
    private Token token;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if(!handlerMethod.hasMethodAnnotation(Teacher.class) && !handlerMethod.hasMethodAnnotation(Student.class)) {
            return true;
        }

        Role userRole = Role.valueOf(request.getAttribute("userRole").toString());

        if(handlerMethod.hasMethodAnnotation(Teacher.class) && userRole.equals(Role.TEACHER)) {
            return true;
        }

        if(handlerMethod.hasMethodAnnotation(Student.class) && userRole.equals(Role.STUDENT)) {
            return true;
        }

        throw new ResponseStatusException(HttpStatusCode.valueOf(403), "You do not have permission to access this function.");
    }
}
