package api.educai.services;

import api.educai.entities.Classroom;
import api.educai.entities.Post;
import api.educai.repositories.ClassroomRepository;
import api.educai.repositories.PostRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ClassroomRepository classroomRepository;

    public Post createPost(Post post, String classroomId) {
        Classroom classroom = classroomRepository.findById(new ObjectId(classroomId));
        classroom.addPost(post);
        classroomRepository.save(classroom);

        Post postSalvo = postRepository.save(post);
        return postSalvo;
    }

    public List<Post> getPosts() {
        List<Post> posts = postRepository.findAll();
        if (posts.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204), "No posts found!");
        }
        return posts;
    }

    public Post getPostById(String id){
        ObjectId idPost = new ObjectId(id);
        Post post = postRepository.findById(idPost);
        if(post == null){
            throw new ResponseStatusException(HttpStatusCode.valueOf(204), "No posts found with that id");
        }
        return post;
    }

    public Post updatePost(String id, String titulo){
        ObjectId idPost = new ObjectId(id);
        postRepository.updateTitle(idPost, titulo);

        Post post = postRepository.findById(idPost);
        return post;
    }

    public Post deletePost(String id){
        ObjectId idPost = new ObjectId(id);
        return postRepository.deleteById(idPost);
    }
}
