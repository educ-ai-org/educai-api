package api.educai.repositories;

import api.educai.entities.TokenBlacklist;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenBlacklistRepository extends MongoRepository<TokenBlacklist, Long> {
    
}
