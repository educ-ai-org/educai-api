package api.educai.filters;

import api.educai.enums.Role;
import api.educai.repositories.UserRespository;
import api.educai.services.UserService;
import api.educai.utils.annotations.Authorized;
import api.educai.utils.token.Token;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private Token token;

    @Autowired
    private UserRespository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

//        HandlerMethod handlerMethod = (HandlerMethod) getHandler(request);
//
//        if (handlerMethod == null || !handlerMethod.hasMethodAnnotation(Authorized.class)) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        String authorization = request.getHeader("Authorization");

        if (authorization == null || authorization.isBlank()) {
            response.setStatus(401);
            response.getWriter().write("Token is not present in header 'Authorization'.");
            return;
        }

        String requestToken = authorization.replace("Bearer ", "");

        ObjectId userId = token.getUserIdByToken(requestToken);
        Role userRole = token.getUserRoleByToken(requestToken);

        request.setAttribute("userId", userId);
        request.setAttribute("userRole", userRole);

        filterChain.doFilter(request, response);
    }
}
