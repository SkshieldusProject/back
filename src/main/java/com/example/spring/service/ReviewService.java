package com.example.spring.service;

import com.example.spring.dto.ReviewDto;
import com.example.spring.entity.Review;
import com.example.spring.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public Review findReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
    }

    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    public Review updateReview(Long reviewId, Review updatedReview) {
        Review existingReview = findReviewById(reviewId);
        existingReview.setScore(updatedReview.getScore());
        existingReview.setSubject(updatedReview.getSubject());
        existingReview.setContent(updatedReview.getContent());
        return reviewRepository.save(existingReview);
    }

    public void deleteReview(Long reviewId) {
        Review existingReview = findReviewById(reviewId);
        reviewRepository.delete(existingReview);
    }
}
