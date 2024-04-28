package api.educai.services.token;

import api.educai.dto.UserDetailsDTO;
import api.educai.entities.TokenBlacklist;
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
public class RefreshToken implements IToken {
    @Value("${jwt.refreshToken.secret}")
    private String refreshSecretKey;

    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;

    @Override
    public String getToken(UserDetailsDTO user) {
        try {
            Instant exp = LocalDateTime.now().plusDays(15).toInstant(ZoneOffset.of("-03:00")); //Expires in 15 days

            return JWT.create()
                    .withClaim("id", user.getId().toString())
                    .withExpiresAt(exp)
                    .sign(Algorithm.HMAC256(refreshSecretKey));
        } catch(JWTCreationException ex) {
            throw new RuntimeException("Error while generating refresh token");
        }
    }

    @Override
    public ObjectId getUserIdByToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(refreshSecretKey)).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        ObjectId userId = new ObjectId(decodedJWT.getClaims().get("id").asString());

        return userId;
    }

    @Override
    public Date getTokenExpirationTime(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(refreshSecretKey)).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        return decodedJWT.getExpiresAt();
    }

    @Override
    public boolean isTokenBlacklisted(ObjectId userId, String token) {
        Optional<TokenBlacklist> tokenBlacklist = tokenBlacklistRepository.findById(userId);

        if(tokenBlacklist.isEmpty()) {
            return false;
        }

        return tokenBlacklistRepository.existsByIdAndRefreshTokenListToken(userId, token);
    }
}
