package com.esioverflow.api.profile;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Profile {

    @Id
    @GeneratedValue
    private Integer Id;

    @Column(length = 150)
    private String bio;

    @Column
    private Boolean confirmed = false;

    @Column
    private Integer reputation;

    @Column
    private String photo; //reprente the url of profile photo

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
