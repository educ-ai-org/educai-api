package api.educai.services;

import api.educai.dto.user.UserDetailsDTO;
import api.educai.entities.User;
import api.educai.repositories.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {
    @Autowired
    private UserRespository userRespository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRespository.findByEmail(username);

        if(user == null) {
            throw new UsernameNotFoundException("E-mail: %s not found".formatted(username));
        }

        return new UserDetailsDTO(user);
    }
}
