package api.educai.dto;

import java.util.Date;

public class InvalidTokenDTO {
    private String token;
    private Date expirationDateTime;

    public InvalidTokenDTO(String token, Date expirationDateTime) {
        this.token = token;
        this.expirationDateTime = expirationDateTime;
    }

    public String getToken() {
        return token;
    }

    public Date getExpirationDateTime() {
        return expirationDateTime;
    }
}
