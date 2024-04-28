package api.educai.controllers;

import api.educai.entities.Post;
import api.educai.services.PostService;
import api.educai.utils.annotations.Authorized;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("post")
public class PostController {
    @Autowired
    private PostService postService;

    @Secured("ROLE_TEACHER")
    @PostMapping
    public ResponseEntity<Post> createPost(@Valid @RequestBody Post post, @RequestHeader("classroom-id") String classroomId){
        post.setId(new ObjectId());
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
    @PatchMapping("/{id}/{titulo}")
    public  ResponseEntity<Post> updateTituloPost(@PathVariable String id, @PathVariable String titulo){
        return ResponseEntity.status(200).body(postService.updatePost(id, titulo));
    }

    @Secured("ROLE_TEACHER")
    @DeleteMapping("/{id}")
    public  ResponseEntity<Post> deletePost(@PathVariable String id){
        return ResponseEntity.status(200).body(postService.deletePost(id));
    }
}
