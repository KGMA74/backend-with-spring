package com.esioverflow.api.post;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;

public record PostDto(
    String title,
     
    String body,

    @JsonProperty("owner-id")
    @NotEmpty
    Integer ownerId,

    @JsonProperty("category-name")
    @NotEmpty
    String category
) {

}
