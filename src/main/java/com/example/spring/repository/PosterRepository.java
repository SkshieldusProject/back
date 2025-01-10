package com.example.spring.repository;

import com.example.spring.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PosterRepository extends JpaRepository<Movie, Long> {

}