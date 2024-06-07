package api.educai.dto.auth;

public class AuthDTO {
    private String token;
    private String refreshToken;

    public AuthDTO(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
