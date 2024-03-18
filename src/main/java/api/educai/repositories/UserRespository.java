package api.educai.repositories;

import api.educai.entities.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRespository extends MongoRepository <User, Long> {
    boolean existsByEmail(String email);
    User findByEmail(String email);
    User findById(ObjectId userId);
}
