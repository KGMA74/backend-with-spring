package com.esioverflow.api.tag;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;



@RestController
public class TagController {
    
    private final TagService tagService;

    //bean dependancies injection
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    // ---- endpoints -----------
    @GetMapping("/tags")
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/tags/{tag-id}")
    public Tag getTag(
        @PathVariable("tag-id") String id
    ) {
        return tagService.getTag(id);
    }
    
    @PostMapping("/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(
        @RequestBody TagDto dto
    ) {
        tagService.createTag(dto);
    }
    
    
}
