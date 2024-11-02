package com.esioverflow.api.tag;

import org.springframework.stereotype.Service;

@Service
public class TagMapper {
    Tag tagDtoToTag(TagDto dto){
        return new Tag(
            dto.name(),
            dto.description()
        );
    }
}
