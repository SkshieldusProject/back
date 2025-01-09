package com.example.spring.controller;

import com.example.spring.dto.MovieDto;
import com.example.spring.dto.PostDto;
import com.example.spring.dto.UserDto;
import com.example.spring.service.MovieService;
import com.example.spring.service.PostService;
import com.example.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final MovieService movieService;
    private final UserService userService;

    @GetMapping("/main")
    public ResponseEntity<?> main() {
        List<MovieDto> movies = movieService.getNowMovies(LocalDate.now());
        List<PostDto> posts = postService.getRecentPosts();


        return ResponseEntity.ok(Map.of(
                "movies", movies,
                "posts", posts
        ));
    }

    //@GetMapping("/main/movies")

    @PostMapping("/post/create")
    public ResponseEntity<?> createPost(@RequestParam String title, @RequestParam String content,
                                        @RequestParam String movieTitle, Authentication authentication) {
        try {
            // 작성자 정보 가져오기
            String currentUser = authentication.getName();
            UserDto userDto = userService.getOneUser(currentUser);

            // 영화 정보 가져오기
            MovieDto movieDto = movieService.getOneMovie(movieTitle);
            postService.create(PostDto.builder()
                    .subject(title)
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
}
