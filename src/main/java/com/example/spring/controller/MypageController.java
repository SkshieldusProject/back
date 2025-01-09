package com.example.spring.controller;


import com.example.spring.dto.ReviewDto;
import com.example.spring.dto.UserDto;
import com.example.spring.entity.Review;
import com.example.spring.service.ReviewService;
import com.example.spring.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/mypage")
public class MypageController {

    private final UserService userService;
    private final ReviewService reviewService;

    public MypageController(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }

    // 마이페이지 조회
    @GetMapping("/uid:{userId}")
    public ResponseEntity<?> getMyPage(@PathVariable String userId, Authentication authentication) {
        // 인증된 사용자 ID 가져오기
        String authenticatedUserId = authentication.getName(); // username으로 설정된 값

        // 사용자 정보 조회
        UserDto requestedUser = userService.getOneUser(userId);
        if (requestedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 사용자를 찾을 수 없습니다.");
        }

        // 본인 여부 확인
        boolean isOwner = userId.equals(authenticatedUserId);

        // 리뷰 목록 조회
        List<ReviewDto> userReviews = userService.getUserReviews(requestedUser);

        // 결과 반환
        return ResponseEntity.ok(Map.of(
                "reviews", userReviews,
                "isOwner", isOwner
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
}


/*
public class MyPageController {

    private final UserService userService;
    private final ReviewService reviewService;

    public MyPageController(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }



    // 1. 마이페이지 조회
    @GetMapping("/uid:{userId}")
    public ResponseEntity<?> getMyPage(@PathVariable Long userId, Authentication authentication) {
        Long authenticatedUserId = userService.getAuthenticatedUserId(authentication);
        boolean userExists = userService.existsById(userId);

        if (!userExists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 사용자를 찾을 수 없습니다.");
        }

        List<ReviewDto> userReviews = reviewService.findReviewsByUserId(userId)
                .stream()
                .map(ReviewDto::fromEntity)
                .toList();

        boolean isOwner = userId.equals(authenticatedUserId);

        return ResponseEntity.ok(Map.of(
                "reviews", userReviews,
                "isOwner", isOwner
        ));
    }

    //인증으로 id 찾아 검색

    // 2. 글 작성
    @PostMapping("/reviews")
    public ResponseEntity<?> createReview(@RequestBody ReviewDto reviewDto, Authentication authentication) {
        Long authenticatedUserId = userService.getAuthenticatedUserId(authentication);

        // 작성자 설정
        reviewDto.setUserId(authenticatedUserId);

        // 글 작성
        Review createdReview = reviewService.createReview(reviewDto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(ReviewDto.fromEntity(createdReview));
    }

    // 3. 글 수정
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable Long reviewId, @RequestBody ReviewDto reviewDto, Authentication authentication) {
        Long authenticatedUserId = userService.getAuthenticatedUserId(authentication);

        // 수정하려는 리뷰 확인 및 권한 검증
        Review existingReview = reviewService.findReviewById(reviewId);
        if (!existingReview.getUser().getId().equals(authenticatedUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
        }

        // 리뷰 수정
        Review updatedReview = reviewService.updateReview(reviewId, reviewDto.toEntity());
        return ResponseEntity.ok(ReviewDto.fromEntity(updatedReview));
    }

    // 4. 글 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId, Authentication authentication) {
        Long authenticatedUserId = userService.getAuthenticatedUserId(authentication);

        // 삭제하려는 리뷰 확인 및 권한 검증
        Review existingReview = reviewService.findReviewById(reviewId);
        if (!existingReview.getUser().getId().equals(authenticatedUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }

        // 리뷰 삭제
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok("리뷰가 삭제되었습니다.");
    }
}
 */