package com.esioverflow.api.post;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;



@RestController
public class PostController {
    private final PostService postService;


    //bean injections
    public PostController(PostService postService) {
        this.postService = postService;
    }

    //-------
    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(
        @Valid @RequestBody PostDto dto
    ) {
        return postService.createPost(dto);
    }
    
    @GetMapping("/posts/{post-id}")
    public Post retrievePost(
        @PathVariable("post-id") Integer  id
    ) {
        return postService.getPostById(id);
    }

    @DeleteMapping("/posts/{post-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(
        @PathVariable("post-id") Integer  id
    ) {
        postService.deletePostById(id);
    }
    

}
