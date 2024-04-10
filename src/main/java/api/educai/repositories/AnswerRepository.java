package api.educai.repositories;

import api.educai.entities.Answer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnswerRepository extends MongoRepository <Answer, Long> {
    Answer findById(ObjectId id);

}
