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
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, columnDefinition = "float default 0.0")
    private Float score;

    @Column(length = 256, nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createDate;

    @Column(columnDefinition = "bigint default 0")
    private long recommendation;

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private User user;

    @Builder
    public Review (Float score, String subject, String content, LocalDateTime createDate, Movie movie, User user) {
        this.score = score;
        this.subject = subject;
        this.content = content;
        this.createDate = createDate;
        this.movie = movie;
        this.user = user;
    }



}
