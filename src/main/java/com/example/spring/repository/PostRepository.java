package com.example.spring.repository;

import com.example.spring.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreateDateDesc();

    List<Post> findByUserId(Long id);
}
