package com.example.spring.controller;

import com.example.spring.dto.MovieDto;
import com.example.spring.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class PosterController {
    private final MovieService movieService;

    @GetMapping("")
    public ResponseEntity<?> getAllMovies() {
        try {
            List<MovieDto> movieList = movieService.getAllMovies();
            return ResponseEntity.ok(Map.of("movies", movieList));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Movie not found") ;
        }
    }

    //영화 상세정보 url 수정
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getMovieDetails(@PathVariable long id) {
        try {
            return ResponseEntity.ok(movieService.getOneMovieReviews(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Movie not found with ID: " + id);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchMovies(@RequestParam("keyword") String keyword) {
        List<MovieDto> movieDtos = movieService.getSearchMovies(keyword);

        return ResponseEntity.ok(Map.of("movies", movieDtos));
    }
}
