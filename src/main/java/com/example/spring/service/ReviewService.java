package com.example.spring.service;

import com.example.spring.dto.ReviewDto;
import com.example.spring.entity.Review;
import com.example.spring.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService{

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserService userService;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> findReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    public ReviewDto findReviewById(Long reviewId) {
        Optional<Review> oReview = reviewRepository.findById(reviewId);
        if (oReview.isPresent()) {
            return ReviewDto.fromEntity(oReview.get());
        }
        else {
            throw  new IllegalArgumentException("리뷰를 찾을 수 없습니다.");
        }
    }

    public void createReview(Review review) {
        reviewRepository.save(review);
    }

    public Review updateReview(Long reviewId, Review updatedReview) {
        ReviewDto existingReview = findReviewById(reviewId);
        existingReview.setScore(updatedReview.getScore());
        existingReview.setSubject(updatedReview.getSubject());
        existingReview.setContent(updatedReview.getContent());
        return reviewRepository.save(existingReview.toEntity());
    }

    public void deleteReview(Long reviewId) {
        ReviewDto existingReview = findReviewById(reviewId);
        reviewRepository.delete(existingReview.toEntity());
    }
}
