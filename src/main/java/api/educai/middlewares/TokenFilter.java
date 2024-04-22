package api.educai.middlewares;

import api.educai.entities.User;
import api.educai.enums.Role;
import api.educai.repositories.UserRespository;
import api.educai.utils.token.Token;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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

        User user = userRepository.findById(userId);

        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
