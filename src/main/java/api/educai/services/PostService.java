package api.educai.services;

import api.educai.adapters.UserAdapter;
import api.educai.entities.Post;
import api.educai.entities.User;
import api.educai.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class PostService {
    @Autowired
    private PostRepository postRepository;

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getPosts() {
        List<Post> posts = postRepository.findAll();
        if (posts.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204), "No posts found!");
        }
        return posts;
    }
}
