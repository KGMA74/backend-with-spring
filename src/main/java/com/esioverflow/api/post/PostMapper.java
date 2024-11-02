package com.esioverflow.api.post;

import org.springframework.stereotype.Service;

import com.esioverflow.api.category.Category;
import com.esioverflow.api.user.User;

@Service
public class PostMapper {

    public Post postDtoToPost(PostDto dto) {

        User owner = new User();
        owner.setId(dto.ownerId());

        Category category = new Category();
        category.setName(dto.category());

        return new Post(
            dto.title(), 
            dto.body(), 
            owner,
            category
        );
    }
    
}
