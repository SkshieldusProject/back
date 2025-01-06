package com.example.spring.repository;

import com.example.spring.dto.ReviewDto;
import com.example.spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);

    List<User> findByEmailAndPhoneNumber(String email, String phoneNumber);
}
