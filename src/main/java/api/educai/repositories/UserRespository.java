package api.educai.repositories;

import api.educai.entities.User;
import api.educai.enums.Role;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;

public interface UserRespository extends MongoRepository <User, Long> {
    boolean existsByEmail(String email);
    boolean existsById(ObjectId id);
    User findByEmail(String email);
    User findById(ObjectId userId);
    void deleteById(ObjectId id);

    @Query("{'id': ?0}")
    @Update("{'$set': { 'name': ?1, 'email': ?2 }}")
    void updateEmailAndName(ObjectId id, String name, String email);

    @Query("{'id': ?0}")
    @Update("{'$push': { 'classrooms': ?1 }}'")
    void addClassroom(ObjectId id, ObjectId classroomId);

    List<User> findByRoleAndClassroomsIdOrderByScoreDesc(Role role, ObjectId classroomId);


}
