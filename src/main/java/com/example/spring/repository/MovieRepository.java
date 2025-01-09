package com.example.spring.repository;

import com.example.spring.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT m FROM Movie m WHERE YEAR(m.releaseDate) = :year AND MONTH(m.releaseDate) = :month")
    List<Movie> findByNowshowing(@Param("year") int nowYear ,@Param("month") int nowMonth);

    Optional<Movie> findFirstByTitle(String movieTitle);
}
