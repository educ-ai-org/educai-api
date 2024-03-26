package api.educai.repositories;

import api.educai.entities.Classroom;
import api.educai.entities.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

public interface ClassroomRepository extends MongoRepository <Classroom, Long> {
    Classroom findById(ObjectId id);
}
