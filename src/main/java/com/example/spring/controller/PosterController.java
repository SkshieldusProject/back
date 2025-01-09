package com.example.spring.controller;

import com.example.spring.dto.MovieDto;
import com.example.spring.service.PosterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/movie/posters")
public class PosterController {
    private final PosterService posterService;

    public PosterController(PosterService posterService) {
        this.posterService = posterService;
    }

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

    // 특정 영화 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getPosterById(@PathVariable long id) {
        try {
            MovieDto posterUrl = posterService.getPosterPathById(id); // DTO 반환
            return ResponseEntity.ok(posterUrl);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Poster not found with ID: " + id);
        }
    }
}
