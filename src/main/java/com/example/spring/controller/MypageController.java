package com.example.spring.controller;


import com.example.spring.dto.MovieDto;
import com.example.spring.dto.PostDto;
import com.example.spring.dto.ReviewDto;
import com.example.spring.dto.UserDto;
import com.example.spring.entity.Review;
import com.example.spring.service.MovieService;
import com.example.spring.service.PostService;
import com.example.spring.service.ReviewService;
import com.example.spring.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/mypage")
public class MypageController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final PostService postService;
    private final MovieService movieService;

    public MypageController(UserService userService, ReviewService reviewService, PostService postService) {
        this.userService = userService;
        this.reviewService = reviewService;
        this.postService = postService;
        this.movieService = new MovieService();
    }
    // 마이페이지 조회
    @GetMapping("/uid/{userId}")
    public ResponseEntity<?> getMyPage(@PathVariable String userId) {
        System.out.println("유저id로 검색 : "+ userId);
        UserDto requestedUser = userService.getOneUser(userId);
        System.out.println("유저DTO 할당");
        if (requestedUser == null) {
            System.out.println("사용자 못 찾음");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 사용자를 찾을 수 없습니다.");
        }

        // 리뷰 목록 조회
        List<ReviewDto> userReviews = userService.getUserReviews(requestedUser);


        // 결과 반환
        return ResponseEntity.ok(Map.of(
                "reviews", userReviews
        ));
    }

    //게시글 조회
    @GetMapping("/uid/{userId}/mypost")
    public ResponseEntity<?> getMyPost(@PathVariable String userId) {
        UserDto requestedUser = userService.getOneUser(userId);
        if (requestedUser == null) {
            // 사용자 존재하지 않음
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 사용자를 찾을 수 없습니다.");
        }

        // 게시글 목록 조회
        List<PostDto> userPosts = postService.getPostsByUser(requestedUser);

        // 결과 반환
        return ResponseEntity.ok(Map.of(
                "posts", userPosts
        ));
    }

    // 글 작성
    @PostMapping("/reviews")
    public ResponseEntity<?> createReview(@RequestBody ReviewDto reviewDto, Authentication authentication) {
        String authenticatedUserId = authentication.getName();

        // 작성자 설정
        UserDto currentUser = userService.getOneUser(authenticatedUserId);
        reviewDto.setUser(currentUser.toEntity());

        // 글 작성
        Review createdReview = reviewService.createReview(reviewDto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(ReviewDto.fromEntity(createdReview));
    }

    // 글 수정
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable Long reviewId, @RequestBody ReviewDto reviewDto, Authentication authentication) {
        String authenticatedUserId = authentication.getName();

        // 수정하려는 리뷰 확인
        Review existingReview = reviewService.findReviewById(reviewId);

        // 권한 확인
        if (existingReview.getUser().getId() != Long.parseLong(authenticatedUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
        }

        // 리뷰 수정
        Review updatedReview = reviewService.updateReview(reviewId, reviewDto.toEntity());
        return ResponseEntity.ok(ReviewDto.fromEntity(updatedReview));
    }


    // 글 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId, Authentication authentication) {
        String authenticatedUserId = authentication.getName();

        // 삭제하려는 리뷰 확인
        Review existingReview = reviewService.findReviewById(reviewId);

        if (existingReview.getUser().getId() != Long.parseLong(authenticatedUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }

        // 리뷰 삭제
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok("리뷰가 삭제되었습니다.");
    }

    @PostMapping("/reviews/{movieId}/create")
    public ResponseEntity<?> createReview(@PathVariable long movieId , @RequestParam float score, @RequestParam String content,
                                          Authentication authentication) {
        try{
            String currentUser = authentication.getName();
            UserDto userDto = userService.getOneUser(currentUser);

            MovieDto movieDto = movieService.getOneMovieById(movieId);
            ReviewDto reviewDto = ReviewDto.builder()
                    .score(score)
                    .subject("사용안함")
                    .content(content)
                    .createDate(LocalDateTime.now())
                    .movie(movieDto.toEntity())
                    .user(userDto.toEntity())
                    .build();
            reviewService.createReview(reviewDto.toEntity());
            return ResponseEntity.ok("review created!!");

        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
