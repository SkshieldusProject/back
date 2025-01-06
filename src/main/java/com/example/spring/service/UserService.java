package com.example.spring.service;

import com.example.spring.dto.UserDto;
import com.example.spring.entity.User;
import com.example.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


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
}
