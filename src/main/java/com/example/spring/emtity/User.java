package com.example.spring.emtity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Table(name="userTbl")
@Getter
@Setter
@NoArgsConstructor
@Entity
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable = false) // name="컬럼명 변경"
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate registerDate;

    @OneToMany
    private List<Review> reviews;
}

