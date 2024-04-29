package api.educai.repositories;

import api.educai.entities.Classroom;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClassroomRepository extends MongoRepository <Classroom, Long> {
    Classroom findById(ObjectId id);
}
