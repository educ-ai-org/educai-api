package api.educai.utils.token;

import api.educai.entities.User;
import org.bson.types.ObjectId;

import java.util.Date;

public interface IToken {
    String getToken(User user);
    ObjectId getUserIdByToken(String token);
    Date getTokenExpirationTime(String token);
}
