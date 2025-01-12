package com.example.spring.controller;


import com.example.spring.dto.UserDto;
import com.example.spring.service.ReviewService;
import com.example.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;


    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    // 폼 형식
    @PostMapping("/signup_process")
    public ResponseEntity<String> signup(@RequestParam String userId, @RequestParam String password,
    @RequestParam String email, @RequestParam String phoneNumber) {
        logger.info("회원가입 시도");
        logger.info(userId);
        logger.info(password);
        logger.info(email);
        logger.info(phoneNumber);
        try{
            userService.register(UserDto.builder()
                            .userId(userId)
                            .password(password)
                            .email(email)
                            .phoneNumber(phoneNumber)
                            .registerDate(LocalDate.now())
                            .build());
            return ResponseEntity.ok("Signup successful");
        } catch (DataIntegrityViolationException e) {
            logger.info("회원가입 실패");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 개인정보 수정하기
    @PutMapping("/modify")
    public ResponseEntity<String> modify(@RequestParam String phoneNumber, @RequestParam String password) {
        logger.info(phoneNumber);
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();
            System.out.println(userId);
            UserDto userDto = userService.getOneUser(userId);
            System.out.println("모디파이에서 가져온 dto "+ userDto.toString());
            userDto.setPhoneNumber(phoneNumber);
            userDto.setPassword(password);
            userService.modify(userDto);
            return ResponseEntity.ok("Modify successful");

        }catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 아이디 찾기
    @PostMapping("/findId")
    public ResponseEntity<?> findId(@RequestParam String email, @RequestParam String phoneNumber) {
        try{
            List<UserDto> userDtos = userService.findUserByEmailAndPhoneNumber(email, phoneNumber);
            return ResponseEntity.ok(Map.of(
                    "users", userDtos
            ));
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(null);
        }

    }

    // 아이디 삭제하기
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(Authentication authentication) {
        try{
            String userId = authentication.getName();
            UserDto userDto = userService.getOneUser(userId);
            userService.deleteUser(userDto);
            return ResponseEntity.ok("Delete successful");
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
