package com.example.spring.service;

import com.example.spring.dto.MovieDto;
import com.example.spring.entity.Movie;
import com.example.spring.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public List<MovieDto> getNowMovies(LocalDate now) {
        int nowMonth = now.getMonthValue();
        int nowYear = now.getYear();
        List<Movie> movies = movieRepository.findByNowshowing(nowYear ,nowMonth);
        List<MovieDto> movieDtos = new ArrayList<>();
        for(Movie movie : movies) {
            movieDtos.add(MovieDto.builder()
                            .id(movie.getId())
                            .title(movie.getTitle())
                            .plot(movie.getPlot())
                            .releaseDate(movie.getReleaseDate())
                            .actors(movie.getActors())
                            .genres(movie.getGenres())
                            .movieReviews(movie.getMovieReviews())
                            .moviePosts(movie.getMoviePosts())
                    .build());
            }
        return movieDtos;
    }

    public MovieDto getOneMovie(String movieTitle) {
        Optional<Movie> oMovie = movieRepository.findFirstByTitle(movieTitle);

        // 필요한 값만 추가해서 리턴
        return MovieDto.builder()
                .id(oMovie.get().getId())
                .director(oMovie.get().getDirector())
                .title(oMovie.get().getTitle())
                .plot(oMovie.get().getPlot())
                .releaseDate(oMovie.get().getReleaseDate())
                .actors(oMovie.get().getActors())
                .genres(oMovie.get().getGenres())
                .movieReviews(oMovie.get().getMovieReviews())
                .moviePosts(oMovie.get().getMoviePosts())
                .build();
    }
}
