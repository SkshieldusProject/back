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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final PostService postService;
    private final MovieService movieService;


    //내가 작성한 게시글 조회
    @GetMapping("/uid/mypost")
    public ResponseEntity<?> getMyPosts(Authentication authentication) {
        String userId = authentication.getName();
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

    //내가 작성한 리뷰 조회
    @GetMapping("/uid/myreviews")
    public ResponseEntity<?> getMyReviews(Authentication authentication) {
        String userId = authentication.getName();
        UserDto requestedUser = userService.getOneUser(userId);
        if (requestedUser == null) {
            // 사용자 존재하지 않음
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 사용자를 찾을 수 없습니다.");
        }

        // 게시글 목록 조회
        List<ReviewDto> userReviews = userService.getUserReviews(requestedUser);
        for (ReviewDto userReview : userReviews) {
            userReview.setUser(null);
        }

        // 결과 반환
        return ResponseEntity.ok(Map.of(
                "reviews", userReviews
        ));
    }


    // 리뷰 수정
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable Long reviewId, @RequestParam float score, @RequestParam String content,
                                          Authentication authentication) {
        String authenticatedUserId = authentication.getName();

        // 수정하려는 리뷰 확인
        ReviewDto existingReview = reviewService.findReviewById(reviewId);

        // 권한 확인
        if (!authenticatedUserId.equals(existingReview.getUser().getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
        }

        existingReview.setScore(score);
        existingReview.setContent(content);
        // 리뷰 수정
        reviewService.updateReview(existingReview.toEntity());
        return ResponseEntity.ok("Review Modify Success");
    }


    // 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId, Authentication authentication) {
        String authenticatedUserId = authentication.getName();

        // 삭제하려는 리뷰 확인
        ReviewDto existingReview = reviewService.findReviewById(reviewId);
        System.out.println(existingReview.getUser().getUserId());
        if (!authenticatedUserId.equals(existingReview.getUser().getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }

        // 리뷰 삭제
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok("리뷰가 삭제되었습니다.");
    }


    // 리뷰 작성 -> 이걸로 동작
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
