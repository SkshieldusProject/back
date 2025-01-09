package com.example.spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 50, nullable = false)
    private String director;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String plot;

    @Column(nullable = false)
    private LocalDate releaseDate;

    @ManyToMany
    @JoinTable(
            name = "movieactor",
            joinColumns = @JoinColumn(name = "movie_id"), // 무비의 기본 키
            inverseJoinColumns = @JoinColumn(name = "cast_id") // 캐스트의 기본 키
    )
    private List<Actor> actors;


    @ManyToMany
    @JoinTable(
            name = "moviegenre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Review> movieReviews;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Post> moviePosts;

    @Builder
    public Movie(long id, String director, String title, String plot, LocalDate releaseDate
    , List<Review> movieReviews, List<Post> moviePosts) {
        this.id = id;
        this.director = director;
        this.title = title;
        this.plot = plot;
        this.releaseDate = releaseDate;
        this.movieReviews = movieReviews;
        this.moviePosts = moviePosts;
    }

}
