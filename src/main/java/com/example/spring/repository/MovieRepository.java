package com.example.spring.repository;

import com.example.spring.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query( "SELECT m  " +
            "FROM Movie m " +
            "WHERE YEAR(m.releaseDate) = :year AND MONTH(m.releaseDate) = :month")
    List<Movie> findByNowshowing(@Param("year") int nowYear ,@Param("month") int nowMonth);

    Optional<Movie> findFirstByTitle(String movieTitle);

    @Query( "SELECT m " +
            "FROM Movie m LEFT JOIN m.movieReviews r " +
            "GROUP BY m " +
            "ORDER BY COALESCE(AVG(r.score), 0) DESC")
    List<Movie> findByRecommended();


    // 쿼리문 아래로 내릴려면 "" + "" 로 해줘야 함
    @Query( "SELECT m " +
            "FROM Movie m LEFT JOIN m.movieReviews r " +
            "WHERE FUNCTION('YEAR', m.releaseDate) = :year AND FUNCTION('MONTH', m.releaseDate) = :month " +
            "GROUP BY m " +
            "ORDER BY COALESCE(AVG(r.score), 0) DESC")
    List<Movie> findByMonthlyRecommended(@Param("year") int year, @Param("month") int month);

    @Query("SELECT COALESCE(AVG(r.score), 0.0) FROM Review r WHERE r.movie.id = :movieId")
    float findScoreByid(@Param("movieId") long id);
}
