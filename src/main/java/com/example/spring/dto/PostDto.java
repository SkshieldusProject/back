package com.example.spring.dto;

import com.example.spring.entity.Movie;
import com.example.spring.entity.Post;
import com.example.spring.entity.User;
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
    private long id;

    private String subject;

    private String content;

    private LocalDateTime createDate;

    private User user;

    private Movie movie;

    public Post toEntity() {
        return Post.builder()
                .id(id)
                .content(content)
                .subject(subject)
                .user(user)
                .movie(movie)
                .createDate(createDate)
                .build();
    }

    public static PostDto fromEntity(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .subject(post.getSubject())
                .user(post.getUser())
                .movie(post.getMovie())
                .createDate(post.getCreateDate())
                .build();
    }
}
