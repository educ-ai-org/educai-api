package api.educai.repositories;

import api.educai.entities.Option;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OptionRepository extends MongoRepository <Option, Long> {
    Option findById(ObjectId id);

}
