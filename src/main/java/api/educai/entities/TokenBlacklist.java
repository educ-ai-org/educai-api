package api.educai.entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class TokenBlacklist {
    private String accessToken;
    private String refreshToken;
    private LocalDateTime expirationDateTime;
    private ObjectId userId;
}
