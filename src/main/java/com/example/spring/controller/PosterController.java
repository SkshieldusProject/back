package com.example.spring.controller;

import com.example.spring.dto.MovieDto;
import com.example.spring.dto.ReviewDto;
import com.example.spring.dto.UserDto;
import com.example.spring.entity.Movie;
import com.example.spring.service.MovieService;
import com.example.spring.service.PosterService;
import com.example.spring.service.ReviewService;
import com.example.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class PosterController {
    private final PosterService posterService;
    private final MovieService movieService;
    private final ReviewService reviewService;
    private final UserService userService;

    // 영화 등록 및 포스터 업로드
    @PostMapping
    public ResponseEntity<?> addPosterPath(
            @RequestParam("title") String title,
            @RequestParam("director") String director,
            @RequestParam("genre") String genre,
            @RequestParam("releaseDate") LocalDate releaseDate,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        try {
            // 요청 데이터를 서비스로 전달
            MovieDto savedMovie = posterService.addMovieWithPoster(title, director, genre, releaseDate, image);
            return ResponseEntity.ok(savedMovie);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Unexpected error: " + e.getMessage());
        }
    }


    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getMovieDetails(@PathVariable Long id) {
        try {
            MovieDto movieDetails = posterService.getPosterPathById(id);

            return ResponseEntity.ok(movieDetails);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Movie not found with ID: " + id);
        }
    }

    @RequestMapping("/api/reviews")
    public class ReviewController {
        private final ReviewService reviewService;

        public ReviewController(ReviewService reviewService) {
            this.reviewService = reviewService;
        }

        @GetMapping("/{movieId}/average-score")
        public ResponseEntity<?> getAverageScore(@PathVariable Long score) {
            double averageScore = posterService.calculateAverageScore(Float.valueOf(score));
            return ResponseEntity.ok(averageScore);
        }
    }
}