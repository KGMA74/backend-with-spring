package com.esioverflow.api.post;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    //constructors dependancies injection
    public PostService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    public Post createPost(PostDto dto) {
        return postRepository.save(
            postMapper.postDtoToPost(dto)
        );
    }

    public Post getPostById(Integer id) {
        return postRepository.findById(id)
                             .orElse(new Post());
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public void deletePostById(Integer id) {
        postRepository.deleteById(id);
    }

    


}
