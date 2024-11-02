package com.esioverflow.api.vote;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@DiscriminatorValue(value = "upvote")
@Entity
public class Upvote extends Vote{
    
}
