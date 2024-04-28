package api.educai.services.token;

import api.educai.dto.UserDetailsDTO;
import api.educai.entities.TokenBlacklist;
import api.educai.enums.Role;
import api.educai.repositories.TokenBlacklistRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

@Service
public class Token implements IToken{
    @Value("${jwt.token.secret}")
    private String tokenSecretKey;
    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;

    @Override
    public String getToken(UserDetailsDTO user) {
        try {
            Instant exp = LocalDateTime.now().plusMinutes(15).toInstant(ZoneOffset.of("-03:00")); //Expires in 15 minutes

            return JWT.create()
                    .withIssuer("educ.ai-api")
                    .withClaim("id", user.getId().toString())
                    .withClaim("role", user.getRole().toString())
                    .withExpiresAt(exp)
                    .sign(Algorithm.HMAC256(tokenSecretKey));
        } catch (JWTCreationException ex) {
            throw new RuntimeException("Error while generating access token");
        }
    }

    @Override
    public ObjectId getUserIdByToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(tokenSecretKey)).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        return new ObjectId(decodedJWT.getClaims().get("id").asString());
    }

    @Override
    public Date getTokenExpirationTime(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(tokenSecretKey)).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        return decodedJWT.getExpiresAt();
    }

    public Role getUserRoleByToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(tokenSecretKey)).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        return Role.valueOf(decodedJWT.getClaims().get("role").asString());
    }

    @Override
    public boolean isTokenBlacklisted(ObjectId userId, String token) {
        Optional<TokenBlacklist> tokenBlacklist = tokenBlacklistRepository.findById(userId);

        if(tokenBlacklist.isEmpty()) {
            return false;
        }

        return tokenBlacklistRepository.existsByIdAndAccessTokenListToken(userId, token);
    }
}
