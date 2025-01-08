package com.example.spring.repository;

import com.example.spring.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 현재 리뷰 엔티티에서 유저를 참조하고 있기 때문에 리뷰 레포지토리에서 유저를 찾을 수 있음
    List<Review> findByUserId(Long userId);
}
