package api.educai.entities;

import api.educai.dto.auth.InvalidTokenDTO;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Document
public class TokenBlacklist {
    private ObjectId id;
    private List<InvalidTokenDTO> accessTokenList;
    private List<InvalidTokenDTO> refreshTokenList;

    public TokenBlacklist(ObjectId id) {
        this.id = id;
        this.accessTokenList = new ArrayList<>();
        this.refreshTokenList = new ArrayList<>();
    }

    public void addAccessToken(String token, Date expirationTime) {
        accessTokenList.add(new InvalidTokenDTO(token, expirationTime));
    }

    public void addRefreshToken(String refreshToken, Date expirationTime) {
        refreshTokenList.add(new InvalidTokenDTO(refreshToken, expirationTime));
    }
}
