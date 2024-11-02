package com.esioverflow.api.vote;



import com.esioverflow.api.post.Post;
import com.esioverflow.api.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) 
@DiscriminatorColumn(name = "vote_type") //only usefull when use SINGLE_TABLE strategy; vote_type = upvote or downvote
@Entity
public class Vote {
    
    //                  TODO
    // use author and post as primary key
    // instead of the integer id
    // cuz we need to make the possibility 
    // to make a vote in given post by
    // a given user only possible one time, 
    // no more
    @EmbeddedId
    private VoteId id;

    @OneToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private Post post;
}
