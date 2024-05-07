package api.educai.repositories;

import api.educai.entities.Classwork;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClassworkRepository extends MongoRepository <Classwork, Long> {
    Classwork findById(ObjectId id);

}
