package api.educai.services.token;

import api.educai.dto.UserDetailsDTO;
import api.educai.entities.User;
import org.bson.types.ObjectId;

import java.util.Date;

public interface IToken {
    String getToken(UserDetailsDTO user);
    ObjectId getUserIdByToken(String token);
    Date getTokenExpirationTime(String token);
    boolean isTokenBlacklisted(ObjectId userId, String token);
}
