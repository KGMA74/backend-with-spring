package com.esioverflow.api.tag;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TagService {
    private final TagMapper tagMapper;
    private final TagRepository tagRepository;

    //bean injections
    public TagService(TagMapper tagMapper, TagRepository tagRepository) {
        this.tagMapper = tagMapper;
        this.tagRepository = tagRepository;
    };

    public List<Tag> getAllTags(){
        return tagRepository.findAll();
    }

    public Tag getTag(String id) {
        return tagRepository.findById(id)
                            .orElse(new Tag());
    }

    public void createTag(TagDto Dto) {
       tagRepository.save(
            tagMapper.tagDtoToTag(Dto)
       );
    }


}
