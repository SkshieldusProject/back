package com.example.spring.dto;


import com.example.spring.entity.Post;
import com.example.spring.entity.Review;
import com.example.spring.entity.User;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class UserDto {
    private Long id;

    private String userId;

    private String password;

    private String email;

    private String phoneNumber;

    private LocalDate registerDate;

    private List<Review> recommendedReviews;

    private List<Review> reviews;

    private List<Post> userPosts;

    public User toEntity() {
        return User.builder()
                .id(this.id)
                .userId(this.userId)
                .password(this.password)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .registerDate(this.registerDate)
                .reviews(this.reviews)
                .userPosts(this.userPosts)
                .build();
    }
}
