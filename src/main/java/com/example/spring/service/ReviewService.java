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

//    public List<ReviewDto> getUserReviews(Long userId){
//        List<Review> reviews = reviewRepository.findByUser_Id(userId);
//        List<ReviewDto> reviewDtos = new ArrayList<ReviewDto>();
//        for(Review review : reviews){
//            reviewDtos.add(ReviewDto.builder()
//                            .id(review.getId())
//                            .user(review.getUser())
//                            .movie(review.getMovie())
//                            .score(review.getScore())
//                            .content(review.getContent())
//                            .recommendationUsers(review.getRecommendationUsers())
//                            .subject(review.getSubject())
//                            .createDate(review.getCreateDate())
//                    .build());
//        }
//        return reviewDtos;
//    }
}
