package com.example.spring.controller;

import com.example.spring.dto.MovieDto;
import com.example.spring.service.MovieService;
import com.example.spring.service.PosterService;
import com.example.spring.service.ReviewService;
import com.example.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movie/posters")
@RequiredArgsConstructor
public class PosterController {
    private final PosterService posterService;
    private final MovieService movieService;
    private final ReviewService reviewService;
    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<?> getAllMovies() {
        try {
            List<MovieDto> movieList = posterService.getAllMovies();
            return ResponseEntity.ok(movieList);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Movie not found") ;
        }
    }


    //영화 등록 및 수정부분 삭제
//    // 영화 등록 및 포스터 업로드
//    @PostMapping
//    public ResponseEntity<?> addPosterPath(
//            @RequestParam("title") String title,
//            @RequestParam("director") String director,
//            @RequestParam("genre") String genre,
//            @RequestParam("releaseDate") LocalDate releaseDate,
//            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
//        try {
//            // 요청 데이터를 서비스로 전달
//            MovieDto savedMovie = posterService.addMovieWithPoster(title, director, genre, releaseDate, image);
//            return ResponseEntity.ok(savedMovie);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Unexpected error: " + e.getMessage());
//        }
//    }


    //영화 상세정보 url 수정
    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieDetails(@PathVariable Long id) {
        try {
            MovieDto movieDetails = posterService.getPosterPathById(id);
            return ResponseEntity.ok(movieDetails);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Movie not found with ID: " + id);
        }
    }

//    @RequestMapping("/api/reviews")
//    public class ReviewController {
//        private final ReviewService reviewService;
//
//        public ReviewController(ReviewService reviewService) {
//            this.reviewService = reviewService;
//        }

    @GetMapping("/{movieId}/average-score")
    public ResponseEntity<?> getScore(@PathVariable Long movieId) {
        float score = movieService.getMovieScore(movieId);
        return ResponseEntity.ok(Map.of("movie", score));
    }

//    @GetMapping("/search")
//    public ResponseEntity<?> searchMovies(@RequestParam("keyword") String keyword) {
//        List<MovieDto> movieDtos = movieService.getSearchMovies(keyword);
//
//        return ResponseEntity.ok(Map.of("movies", movieDtos));
//    }
}