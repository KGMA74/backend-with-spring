package com.esioverflow.api.vote;

import java.io.Serializable;

import com.esioverflow.api.post.Post;
import com.esioverflow.api.user.User;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteId implements Serializable{
    private Post post;
    private User author;
}
