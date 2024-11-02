package com.esioverflow.api.tag;

import java.time.LocalDateTime;
import java.util.List;

import com.esioverflow.api.post.Post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Tag {
    @Id
    @NotEmpty
    private String name;

    @Column(nullable = false)
    @NotEmpty
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(
        name = "updated_at",
        updatable = false
    )
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "tags")
    private List<Post> posts;

    //constructors
  
    public Tag(String name, String description) {
        this.name = name;
        this.description = description;
        this.createdAt = this.updatedAt = LocalDateTime.now();
        
    }
}
