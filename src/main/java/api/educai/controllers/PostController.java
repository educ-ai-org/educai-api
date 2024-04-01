package api.educai.controllers;

import api.educai.entities.Post;
import api.educai.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("post")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<Post> createPost(Post post){
        return ResponseEntity.status(201).body(postService.createPost(post));
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(Post post){
        return ResponseEntity.status(200).body(postService.getPosts());
    }
}
