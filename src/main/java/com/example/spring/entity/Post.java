package com.example.spring.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 256)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @ManyToOne
    private User user;


    @ManyToOne
    private Movie movie;

    @Builder
    public Post(long id, String subject, String content, LocalDateTime createDate, User user, Movie movie) {
        this.id = id;
        this.subject = subject;
        this.content = content;
        this.createDate = createDate;
        this.user = user;
        this.movie = movie;
    }


}
