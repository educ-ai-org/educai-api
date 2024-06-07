package api.educai.filter;

import api.educai.dto.user.UserDetailsDTO;
import api.educai.enums.Role;
import api.educai.repositories.UserRespository;
import api.educai.services.token.Token;
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
        String authorization = request.getHeader("Authorization");

        if (authorization == null || authorization.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestToken = authorization.replace("Bearer ", "");

        ObjectId userId = token.getUserIdByToken(requestToken);
        Role userRole = token.getUserRoleByToken(requestToken);

        if(this.token.isTokenBlacklisted(userId, requestToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        request.setAttribute("userId", userId);
        request.setAttribute("userRole", userRole);

        UserDetailsDTO user = new UserDetailsDTO(userRepository.findById(userId));

        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
