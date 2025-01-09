package com.example.spring.dto;

import com.example.spring.entity.Movie;
import com.example.spring.entity.Post;
import com.example.spring.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
public class PostDto {
    private int id;

    private String subject;

    private String content;

    private LocalDateTime createDate;

    private User user;

    private Movie movie;

    public Post toEntity() {
        return Post.builder()
                .content(content)
                .subject(subject)
                .user(user)
                .movie(movie)
                .createDate(createDate)
                .build();
    }
}
