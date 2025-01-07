package com.example.spring.dto;

import com.example.spring.entity.Movie;
import com.example.spring.entity.Review;
import com.example.spring.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class ReviewDto {
    private long id;

    private Float score;

    private String subject;

    private String content;

    private LocalDateTime createDate;

    private List<User> recommendationUsers;

    private Movie movie;

    private User user;

    public Review toEntity() {
        return Review.builder()
                .id(id)
                .score(score)
                .subject(subject)
                .content(content)
                .createDate(createDate)
                .recommendationUsers(recommendationUsers)
                .movie(movie)
                .user(user)
                .build();
    }
}
