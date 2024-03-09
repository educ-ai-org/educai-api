package api.educai.services;

import api.educai.entities.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class TokenService {
    private static String refreshSecretKey = "bauodBOdqdm´MMdadjn´pandopadnoapñdpoandpa´ndopiaNDPÒdpDADÇADÇAÇçADJANDHNJAWIODUOA";
    private static String tokenSecretKey = "bauodBOdqdm´MMdadjn´pandopadnoapñdpoandpa´ndopiaNDPÒdpDADÇADÇAÇçADJANDHNJAWIODUOA";

    public static String getRefreshToken() {
        long exp = System.currentTimeMillis() + (15 * 24 * 60 * 60 * 1000); //Expires in 15 days

        return JWT.create()
                .withClaim("exp", exp)
                .sign(Algorithm.HMAC256(refreshSecretKey));
    }

    public static String getToken(User user) {
        long exp = System.currentTimeMillis() + (15 * 60 * 1000); //Expires in 15 minutes

        return JWT.create()
                .withClaim("id", user.getId().toString())
                .withClaim("role", user.getRole().toString())
                .withClaim("exp", exp)
                .sign(Algorithm.HMAC256(tokenSecretKey));
    }
}
