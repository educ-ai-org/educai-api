package api.educai.services;

import api.educai.dto.NewPostDTO;
import api.educai.dto.PatchPost;
import api.educai.entities.Classroom;
import api.educai.entities.Post;
import api.educai.repositories.ClassroomRepository;
import api.educai.repositories.PostRepository;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private AzureBlobService azureBlobService;

    public Post createPost(NewPostDTO newPost, MultipartFile file) {
        Classroom classroom = classroomRepository.findById(new ObjectId(newPost.getClassroomId()));
        Post post = mapper.map(newPost, Post.class);

        if (file != null) {
            try {
                String path = azureBlobService.upload(file);
                post.setFile(path);
            } catch (IOException ex) {
                throw new ResponseStatusException(HttpStatusCode.valueOf(500), "Error while trying to upload file!");
            }
        }

        postRepository.save(post);

        classroom.addPost(post);
        classroomRepository.save(classroom);

        return post;
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

    public Post updatePost(ObjectId id, PatchPost updatedPost){

        Post post = postRepository.findById(id);
        post.setTitle(updatedPost.getTitle());
        post.setDescription(updatedPost.getDescription());
        postRepository.save(post);

        return post;
    }

    public Post deletePost(String id){
        ObjectId idPost = new ObjectId(id);
        return postRepository.deleteById(idPost);
    }
}
