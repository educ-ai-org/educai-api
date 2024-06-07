package api.educai.controllers;

import api.educai.dto.post.NewPostDTO;
import api.educai.dto.post.PatchPost;
import api.educai.dto.post.PostDTO;
import api.educai.entities.Post;
import api.educai.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/posts")
@Tag(name = "Posts", description = "API para serviços relacionados a posts.")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private ModelMapper mapper;

    @Secured("ROLE_TEACHER")
    @Operation(summary = "Cria um post")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPost( @RequestParam("file") MultipartFile file,
                                            @RequestParam("title") String title,
                                            @RequestParam(value = "description", required = false) String description,
                                            @RequestParam(value = "datePosting") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datePosting,
                                            @RequestParam(value = "classroomId") String classroomId){
        return ResponseEntity.status(201).body(postService.createPost(new NewPostDTO(title, description, datePosting, classroomId), file));

    }

    @Operation(summary = "Retorna todos os posts")
    @GetMapping
    public ResponseEntity<List<Post>> getPosts(){
        return ResponseEntity.status(200).body(postService.getPosts());
    }

    @Operation(summary = "Retorna um post específico passando seu ID")
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable String id){
        return ResponseEntity.status(200).body(mapper.map(postService.getPostById(id), PostDTO.class));
    }

    @Operation(summary = "Retorna a URL para download de um post específico passando seu ID")
    @GetMapping("/{id}/download")
    public ResponseEntity<String> getPostUrlById(@PathVariable String id){
        return ResponseEntity.status(200).body(postService.getPostUrlById(id));
    }

    @Operation(summary = "Atualiza o título de um post")
    @Secured("ROLE_TEACHER")
    @PatchMapping("/{id}")
    public  ResponseEntity<Post> updateTituloPost(@PathVariable ObjectId id, @RequestBody @Valid PatchPost updatedPost){
        return ResponseEntity.status(200).body(postService.updatePost(id, updatedPost));
    }
    @Operation(summary = "Deleta um post")
    @Secured("ROLE_TEACHER")
    @DeleteMapping("/{id}")
    public  ResponseEntity<Post> deletePost(@PathVariable String id){
        return ResponseEntity.status(200).body(postService.deletePost(id));
    }
}
