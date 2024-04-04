package api.educai.repositories;

import api.educai.entities.Question;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepository extends MongoRepository <Question, Long> {
    Question findById(ObjectId id);

}
