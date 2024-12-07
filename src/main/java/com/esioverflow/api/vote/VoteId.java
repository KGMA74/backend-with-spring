package com.esioverflow.api.vote;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class VoteId implements Serializable {
    private Integer authorId;
    private Integer postId;
}
