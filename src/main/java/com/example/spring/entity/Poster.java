package com.example.spring.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Poster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    private String director;
    private String genre;
    private LocalDate releaseDate;

    // 포스터 이미지 경로 또는 URL
    private String posterPath;
}
