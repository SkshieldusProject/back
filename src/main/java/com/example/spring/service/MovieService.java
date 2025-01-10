package com.example.spring.service;

import com.example.spring.dto.MovieDto;
import com.example.spring.entity.Movie;
import com.example.spring.repository.MovieRepository;
import com.example.spring.repository.ReviewRepository;
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

    @Autowired
    private ReviewRepository reviewRepository;

    // 현재 상영중인 영화 가져오기
    public List<MovieDto> getNowMovies(LocalDate now) {
        int nowMonth = now.getMonthValue();
        int nowYear = now.getYear();
        List<Movie> movies = movieRepository.findByNowshowing(nowYear ,nowMonth);
        List<MovieDto> movieDtos = new ArrayList<>();
        for(Movie movie : movies) {
            movieDtos.add(MovieDto.fromEntity(movie));
        }
        return movieDtos;
    }

    // 영화 이름으로 영화 하나 가져오기
    public MovieDto getOneMovie(String movieTitle) {
        Optional<Movie> oMovie = movieRepository.findFirstByTitle(movieTitle);
        Movie movie = oMovie.orElse(null);
        // 필요한 값만 추가해서 리턴
        return MovieDto.fromEntity(movie);
    }

    // 전체 영화 중 평점이 높은 순으로 가져오기
    public List<MovieDto> getRecommendedMovies() {

        List<Movie> movies = movieRepository.findByRecommended();

        List<MovieDto> movieDtos = new ArrayList<>();
        for(Movie movie : movies) {
            movieDtos.add(MovieDto.fromEntity(movie));
        }
        return movieDtos;
    }

    // 현재 개봉중인 영화 중 평점이 높은 순으로 가져오기
    public List<MovieDto> getMonthRecommendedMovies(int year, int month) {
        List<Movie> movies = movieRepository.findByMonthlyRecommended(year, month);

        List<MovieDto> movieDtos = new ArrayList<>();
        for(Movie movie : movies) {
            movieDtos.add(MovieDto.fromEntity(movie));
        }
        return movieDtos;
    }

    public float getMovieScore(long id) {
        return movieRepository.findScoreByid(id);
    }

    public MovieDto getOneMovieById(long movieId) {
        Optional<Movie> oMovie = movieRepository.findById(movieId);
        Movie movie = oMovie.orElse(null);
        // 필요한 값만 추가해서 리턴
        return MovieDto.fromEntity(movie);
    }
}
