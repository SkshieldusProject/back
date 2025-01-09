package com.example.spring.dto;

import com.example.spring.entity.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@ToString
@Builder
public class MovieDto {
    private long id;

    private String director;

    private String title;

    private String plot;

    private LocalDate releaseDate;

    private List<Actor> actors;

    private List<Genre> genres;

    private List<Review> movieReviews;

    private List<Post> moviePosts;

    public Movie toEntity() {
        return Movie.builder()
                .id(id)
                .director(director)
                .title(title)
                .plot(plot)
                .releaseDate(releaseDate)
                .movieReviews(movieReviews)
                .moviePosts(moviePosts)
                .build();
    }

    public static MovieDto fromEntity(Movie movie) {
        return MovieDto.builder()
                .id(movie.getId())
                .director(movie.getDirector())
                .title(movie.getTitle())
                .plot(movie.getPlot())
                .releaseDate(movie.getReleaseDate())
                .movieReviews(movie.getMovieReviews())
                .moviePosts(movie.getMoviePosts())
                .build();
    }

}
