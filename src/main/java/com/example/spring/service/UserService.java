package com.example.spring.service;

import com.example.spring.dto.UserDto;
import com.example.spring.entity.User;
import com.example.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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
            return UserDto.builder()
                    .id(user.getId())
                    .userId(user.getUserId())
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .registerDate(user.getRegisterDate())
                    .reviews(user.getReviews())
                    .build();
        }
        throw new NoSuchElementException("User not found with userId: " + userId);
    }

    public void modify(UserDto userDto) {
        System.out.println("서비스 " + userDto.toString());
        userRepository.save(userDto.toEntity());
    }
}
