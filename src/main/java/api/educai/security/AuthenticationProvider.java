package api.educai.security;

import api.educai.services.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticationProvider implements AuthenticationManager {
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationProvider(AuthenticationService authenticationService, PasswordEncoder passwordEncoder) {
        this.authenticationService = authenticationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String email = authentication.getName();
        final String password = authentication.getCredentials().toString();

        UserDetails userDetails = this.authenticationService.loadUserByUsername(email);

        if(this.passwordEncoder.matches(password, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("E-mail or password invalids!");
        }
    }
}
