package api.educai.repositories;

import api.educai.entities.TokenBlacklist;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TokenBlacklistRepository extends MongoRepository<TokenBlacklist, Long> {
    Optional<TokenBlacklist> findById(ObjectId id);
    boolean existsByIdAndAccessTokenListToken(ObjectId id, String token);
    boolean existsByIdAndRefreshTokenListToken(ObjectId id, String token);
}
