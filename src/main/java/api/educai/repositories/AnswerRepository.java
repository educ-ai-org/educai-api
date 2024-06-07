package api.educai.repositories;

import api.educai.entities.Answer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AnswerRepository extends MongoRepository <Answer, Long> {

    Answer findById(ObjectId id);

    boolean existsAnswerByUserIdAndClassworkId(ObjectId userId, ObjectId classworkId);

    List<Answer> findAllByClassworkId(ObjectId classworkId);
}
