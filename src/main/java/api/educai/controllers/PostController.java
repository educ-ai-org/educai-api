package api.educai.controllers;

import api.educai.dto.PostDTO;
import api.educai.entities.Post;
import api.educai.services.PostService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @Secured("ROLE_TEACHER")
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody @Valid PostDTO post, @RequestHeader String classroomId){
        return ResponseEntity.status(201).body(postService.createPost(post,classroomId));
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(){
        return ResponseEntity.status(200).body(postService.getPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable String id){
        return ResponseEntity.status(200).body(postService.getPostById(id));
    }

    @Secured("ROLE_TEACHER")
    @PatchMapping("/{id}")
    public  ResponseEntity<Post> updateTituloPost(@PathVariable ObjectId id, @RequestBody @Valid PostDTO updatedPost){
        return ResponseEntity.status(200).body(postService.updatePost(id, updatedPost));
    }

    @Secured("ROLE_TEACHER")
    @DeleteMapping("/{id}")
    public  ResponseEntity<Post> deletePost(@PathVariable String id){
        return ResponseEntity.status(200).body(postService.deletePost(id));
    }
}
