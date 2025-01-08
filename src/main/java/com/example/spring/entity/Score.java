package com.example.spring.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Float score;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "review_id",nullable = false)
    private Review review;

}
