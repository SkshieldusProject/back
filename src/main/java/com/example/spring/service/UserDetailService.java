package com.example.spring.service;

import com.example.spring.entity.User;
import com.example.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        //System.out.println(this.userRepository.findByUserId(userId).toString());
        System.out.println(userId);
        Optional<User> user = userRepository.findByUserId(userId);
        if(user.isPresent()) {
            return user.get();
        }
        else{
            System.out.println("아이디가 없음" + userId);
            throw new IllegalArgumentException(userId);
        }
//        return this.userRepository.findByUserId(userId)
//                .orElseThrow( ()-> new IllegalArgumentException(userId) );
    }
}
