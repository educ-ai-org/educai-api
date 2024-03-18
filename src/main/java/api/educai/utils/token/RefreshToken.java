package api.educai.utils.token;

import api.educai.entities.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.bson.types.ObjectId;

public class RefreshToken implements IToken {
    private static String refreshSecretKey = "bauodBOdqdmMMdadjnpandopadnoapñdpAW67DAWT8Ag8dÇADÇAÇçADJANDHNJAWIODUOA";

    @Override
    public String getToken(User user) {
        long exp = System.currentTimeMillis() + (15 * 24 * 60 * 60 * 1000); //Expires in 15 days

        return JWT.create()
                .withClaim("exp", exp)
                .withClaim("id", user.getId().toString())
                .sign(Algorithm.HMAC256(refreshSecretKey));
    }

    @Override
    public ObjectId getUserIdByToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(refreshSecretKey)).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        ObjectId userId = new ObjectId(decodedJWT.getClaims().get("id").asString());

        return userId;
    }
}
