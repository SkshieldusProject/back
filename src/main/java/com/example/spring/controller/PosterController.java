package com.example.spring.controller;

import com.example.spring.entity.Poster;
import com.example.spring.service.PosterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<?> addPoster(
            @RequestParam("title") String title,
            @RequestParam("director") String director,
            @RequestParam("genre") String genre,
            @RequestParam("releaseDate") LocalDate releaseDate,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            // 이미지 저장
            String imagePath = null;
            if (image != null && !image.isEmpty()) {
                imagePath = posterService.savePosterFile(image);
            }

            // Poster 객체 생성
            Poster poster = Poster.builder()
                    .title(title)
                    .director(director)
                    .genre(genre)
                    .releaseDate(releaseDate)
                    .posterPath(imagePath) // 저장된 이미지 경로
                    .build();

            // Poster 저장
            Poster savedPoster = posterService.savePoster(poster);
            return ResponseEntity.ok(savedPoster);
        } catch (Exception e) {
            e.printStackTrace(); // 디버깅을 위한 로그 출력
            return ResponseEntity.status(500).body("Error saving poster: " + e.getMessage());
        }
    }

    // 특정 영화 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getPosterById(@PathVariable Long id) {
        try {
            Poster poster = posterService.getPosterById(id);
            return ResponseEntity.ok(poster);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Poster not found with ID: " + id);
        }
    }
}
