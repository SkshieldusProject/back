package com.example.spring.service;

import com.example.spring.dto.MovieDto;
import com.example.spring.dto.ReviewDto;
import com.example.spring.entity.Movie;
import com.example.spring.repository.PosterRepository;
import com.example.spring.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
public class PosterService {
    private final PosterRepository posterRepository;
    private final ReviewRepository reviewRepository;
    private Movie movie;

    public PosterService(PosterRepository posterRepository, ReviewRepository reviewRepository) {
        this.posterRepository = posterRepository;
        this.reviewRepository = reviewRepository;
    }

    // 모든 영화 조회
    public List<MovieDto> getAllMovies() {
        return posterRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 엔티티 → DTO 변환
    private MovieDto convertToDto(Movie movie) {
        return MovieDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .director(movie.getDirector())
                .genres(movie.getGenres())
                .releaseDate(movie.getReleaseDate())
                .posterPath(movie.getPosterPath())
                .build();
    }

    // 포스터 파일 저장
    public String savePosterFile(MultipartFile posterFile) throws IOException {
        String uploadDir = "src/main/resources/static/images"; // 파일 저장 경로
        File uploadFolder = new File(uploadDir);

        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        // 고유 파일 이름 생성
        String fileName = System.currentTimeMillis() + "_" + posterFile.getOriginalFilename();
        String filePath = uploadDir + File.separator + fileName;

        posterFile.transferTo(new File(filePath)); // 파일 저장

        return "/images/" + fileName; // 반환 URL
    }

    // 영화 등록 및 포스터 업로드
    public MovieDto addMovieWithPoster(
            String title,
            String director,
            String genre,
            LocalDate releaseDate,
            MultipartFile image) {
        try {
            // 포스터 파일 저장
            String posterPath = null;
            if (image != null && !image.isEmpty()) {
                posterPath = savePosterFile(image);
            }

            // 데이터베이스에 저장
            Movie savedMovie = posterRepository.save(movie);

            // 엔티티 → DTO 변환 후 반환
            return convertToDto(savedMovie);
        } catch (IOException e) {
            throw new RuntimeException("Error saving poster file: " + e.getMessage(), e);
        }
    }

    // ID로 영화 조회 및 포스터 경로 반환
    public MovieDto getPosterPathById(long id) {
        // 데이터베이스에서 영화 조회
        Movie movie = posterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with ID: " + id));

        // 엔티티 → DTO 변환 후 반환
        return convertToDto(movie);
    }

    // 평점 평균 계산 메서드
    public double calculateAverageScore(Float score) {
        // DTO의 getScore() 호출
        OptionalDouble average = OptionalDouble.of(score.floatValue());

        return average.orElse(0.0);
    }

}