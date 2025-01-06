package com.example.spring.service;

import com.example.spring.dto.ReviewDto;
import com.example.spring.entity.Review;
import com.example.spring.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService{

    @Autowired
    private ReviewRepository reviewRepository;

//    public List<ReviewDto> getAllReviews(Long id){
//
//    }
}
