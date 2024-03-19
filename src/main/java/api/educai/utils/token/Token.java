package api.educai.utils.token;

import api.educai.entities.User;
import api.educai.enums.Role;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.bson.types.ObjectId;

public class Token implements IToken{
    private static String tokenSecretKey = "AWDHAUDBdadbabwudADbWUDuidawduadopçawpoiacnacanockno-iI0UOadipdiwPDB9puudaw9d087dwD9";

    public String getToken(User user) {
        long exp = System.currentTimeMillis() + (15 * 60 * 1000); //Expires in 15 minutes

        return JWT.create()
                .withClaim("id", user.getId().toString())
                .withClaim("role", user.getRole().toString())
                .withClaim("exp", exp)
                .sign(Algorithm.HMAC256(tokenSecretKey));
    }

    public ObjectId getUserIdByToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(tokenSecretKey)).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        return new ObjectId(decodedJWT.getClaims().get("id").asString());
    }

    public Role getUserRoleByToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(tokenSecretKey)).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        return Role.valueOf(decodedJWT.getClaims().get("role").asString());
    }
}
