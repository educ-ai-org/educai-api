package api.educai.repositories;

import api.educai.entities.Post;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

public interface PostRepository extends MongoRepository<Post, Long> {
    Post findById(ObjectId id);
    Post deleteById(ObjectId id);

    @Query("{'id': ?0}")
    @Update("{'$set': { 'title': ?1}}")
    void updateTitle(ObjectId id, String titulo);
}
