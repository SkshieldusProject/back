package com.example.spring.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 50, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "actors")
    private List<Movie> movies;

    @Builder
    public Actor(String name, List<Movie> movies) {
        this.name = name;
        this.movies = movies;
    }

}
