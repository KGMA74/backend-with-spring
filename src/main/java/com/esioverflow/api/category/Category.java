package com.esioverflow.api.category;

import java.util.List;

import com.esioverflow.api.post.Post;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Category
 */

@Data
@NoArgsConstructor
@Entity
public class Category {
    @Id
    private String name;

    @Column
    private String description;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    private List<Post> posts;

    // construtors
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }
}