package com.example.spring.service;

import com.example.spring.dto.ReviewDto;
import com.example.spring.dto.UserDto;
import com.example.spring.entity.Review;
import com.example.spring.entity.User;
import com.example.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SessionRegistry sessionRegistry;

    public void register(UserDto userDto) {
        System.out.println(userDto.toString());
        userRepository.save(User.builder()
                        .userId(userDto.getUserId())
                        .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                        .email(userDto.getEmail())
                        .phoneNumber(userDto.getPhoneNumber())
                        .registerDate(userDto.getRegisterDate())
                        .build());
        System.out.println("완료");
    }
    public UserDto getOneUser(String userId) {
        Optional<User> oUser = userRepository.findByUserId(userId);
        if(oUser.isPresent()) {
            User user = oUser.get();
            return UserDto.fromEntity(user);
        }
        throw new NoSuchElementException("User not found with userId: " + userId);
    }

    public List<UserDto> findUserByEmailAndPhoneNumber (String email, String phoneNumber) {
        List<User> users = userRepository.findByEmailAndPhoneNumber(email, phoneNumber);
        if(!users.isEmpty()) {
            List<UserDto> userDtos = new ArrayList<>();
            for(User user : users) {
                UserDto temp = UserDto.fromEntity(user);
                temp.setReviews(null);
                temp.setUserPosts(null);
                userDtos.add(temp);
            }
            return userDtos;
        }
        throw new NoSuchElementException("User not found with email and phoneNumber");
    }

    public void modify(UserDto userDto) {
        System.out.println("서비스 " + userDto.toString());
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userRepository.save(userDto.toEntity());
    }



    public List<ReviewDto> getUserReviews(UserDto userDto) {
        List<Review> reviews = userDto.getReviews();
        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (Review review : reviews) {
            reviewDtos.add(ReviewDto.builder()
                            .id(review.getId())
                            .user(review.getUser())
                            .movie(review.getMovie())
                            .score(review.getScore())
                            .content(review.getContent())
                            .recommendationUsers(review.getRecommendationUsers())
                            .subject(review.getSubject())
                            .createDate(review.getCreateDate())
                    .build());
        }
        return reviewDtos;

    }

    // 사용자 세션 무효화 함수
    private void expireUserSessions(String username) {
        sessionRegistry.getAllPrincipals().forEach(principal -> {
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                if (userDetails.getUsername().equals(username)) {
                    sessionRegistry.getAllSessions(userDetails, false).forEach(SessionInformation::expireNow);
                }
            }
        });
    }

    public void deleteUser(UserDto userDto) {
        // 세션 만료
        expireUserSessions(userDto.getUserId());

        userRepository.delete(userDto.toEntity());

        // SecurityContext 초기화
        SecurityContextHolder.clearContext();
    }
}
