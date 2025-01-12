package com.example.spring.controller;

import com.example.spring.dto.MovieDto;
import com.example.spring.dto.PostDto;
import com.example.spring.dto.UserDto;
import com.example.spring.service.MovieService;
import com.example.spring.service.PostService;
import com.example.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final MovieService movieService;
    private final UserService userService;

    // 메인페이지
    @GetMapping("/main")
    public ResponseEntity<?> main() {
        // 현재 상영중인 영화 나열
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        List<MovieDto> movies = movieService.getMonthRecommendedMovies(year, month);
        for(MovieDto movie : movies) {
            movie.setMoviePosts(null);
            movie.setMovieReviews(null);
        }

        List<PostDto> posts = postService.getRecentPosts();


        return ResponseEntity.ok(Map.of(
                "movies", movies,
                "posts", posts
        ));
    }


    // 이달의 영화

    // 리턴 방식
    // month : 현재 상영중인 영화 평점 순으로 객체 배열
    // all : 전체 영화 평점 순으로 객체 배열
    @GetMapping("/main/movies")
    public ResponseEntity<?> recommendedMovies() {
        try{
            System.out.println("영화 평점" + movieService.getMovieScore(1L));
            // 지금 달 중에서 평점 높은 영화 나열
            int year = LocalDate.now().getYear();
            int month = LocalDate.now().getMonthValue();
            List<MovieDto> monthMovies = movieService.getMonthRecommendedMovies(year, month);
            // 전체 영화 중에서 평점 높은 영화 나열
            for(MovieDto movie : monthMovies) {
                movie.setMoviePosts(null);
                movie.setMovieReviews(null);
            }
            List<MovieDto> bestMovies = movieService.getRecommendedMovies();
            for(MovieDto movie : bestMovies) {
                movie.setMoviePosts(null);
                movie.setMovieReviews(null);
            }
            return ResponseEntity.ok(Map.of(
                    "month", monthMovies,
                    "all", bestMovies
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // 영화 리뷰 게시판
    @GetMapping("/main/posts")
    public ResponseEntity<?> postList() {
        List<PostDto> posts = postService.getRecentPosts();

        return ResponseEntity.ok(Map.of(
                "posts", posts
        ));
    }

    // 영화 게시글 상세보기
    @GetMapping("/main/posts/detail/{id}")
    public ResponseEntity<?> postDetail(@PathVariable Long id) {
        try {
            PostDto postDto = postService.getOnePost(id);
            return ResponseEntity.ok(Map.of(
                    "post", postDto
            ));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 영화 리뷰 게시글 작성
    // 로그인 해야 작성 가능
    @PostMapping("/post/create")
    public ResponseEntity<?> createPost(@RequestParam String subject, @RequestParam String content,
                                        @RequestParam String movieTitle, Authentication authentication) {
        try {
            // 작성자 정보 가져오기
            String currentUser = authentication.getName();
            UserDto userDto = userService.getOneUser(currentUser);

            // 영화 정보 가져오기
            MovieDto movieDto = movieService.getOneMovie(movieTitle);
            postService.create(PostDto.builder()
                    .subject(subject)
                    .content(content)
                    .createDate(LocalDateTime.now())
                    .user(userDto.toEntity())
                    .movie(movieDto.toEntity())
                    .build()
            );
            return ResponseEntity.ok("post created!!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // 게시글 수정하기
    @GetMapping("/posts/modify/{id}")
    public ResponseEntity<?> modifyPost(@PathVariable Long id, Authentication authentication) {
        String authenticationUserId = authentication.getName();

        PostDto postDto = postService.getOnePost(id);

        if(!postDto.getUser().getUsername().equals(authenticationUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
        }

        return ResponseEntity.ok(Map.of(
                "post", postDto
        ));
    }
    @PutMapping("/posts/modify/{id}")
    public ResponseEntity<?> modifyPost(@PathVariable Long id, Authentication authentication,
                                        @RequestParam String subject, @RequestParam String content, @RequestParam String movieTitle) {
        try {
            String authenticationUserId = authentication.getName();

            PostDto postDto = postService.getOnePost(id);

            if (!postDto.getUser().getUsername().equals(authenticationUserId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
            }

            MovieDto movieDto = movieService.getOneMovie(movieTitle);
            postDto.setSubject(subject);
            postDto.setContent(content);
            postDto.setMovie(movieDto.toEntity());

            postService.modify(postDto);
            return ResponseEntity.ok("post modified!!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    // 영화 리뷰 삭제하기
    // 작성한 사용자만 삭제할 수 있음
    @DeleteMapping("/posts/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, Authentication authentication) {
        String authenticationUserId = authentication.getName();

        PostDto postDto = postService.getOnePost(id);

        if(!postDto.getUser().getUsername().equals(authenticationUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }

        postService.deletePost(postDto);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }
}