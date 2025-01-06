package com.example.spring.controller;


import com.example.spring.dto.UserDto;
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
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    // 제이슨 형식
    @PostMapping("/signup1_process")
    public ResponseEntity<String> signup(@RequestBody UserDto userDto) {
        // 입력 이름이 uerDto 형식에 맞으면 잘 들어오는거
        logger.info("회원가입 시도");
        logger.info(userDto.toString());
        try{
            userService.register(UserDto.builder()
                            .userId(userDto.getUserId())
                            .password(userDto.getPassword())
                            .email(userDto.getEmail())
                            .phoneNumber(userDto.getPhoneNumber())
                            .registerDate(LocalDate.now())
                    .build());
            return ResponseEntity.ok("Signup successful");
        } catch (DataIntegrityViolationException e) {
            // user id가 이미 존재하는데 또 가입하려 한다면
            // 또는 널 값이 들어오면다면 데이터 무결성에 위반되어서 해당 에러 출력
            logger.info("회원가입 실패");
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }



    // 폼 형식
    @PostMapping("/signup2_process")
    public ResponseEntity<String> signup2(@RequestParam String userId, @RequestParam String password,
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

    @PostMapping("/modify")
    public ResponseEntity<String> modify(@RequestParam String phoneNumber) {
        logger.info(phoneNumber);
        //logger.info("아이디는"+id);
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();
            System.out.println(userId);
            UserDto userDto = userService.getOneUser(userId);
            System.out.println("모디파이에서 가져온 dto "+ userDto.toString());
            userDto.setPhoneNumber(phoneNumber);
            userService.modify(userDto);
            return ResponseEntity.ok("Modify successful");

        }catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 8


//    @GetMapping("/reviews")
//    public ResponseEntity<String> reviews() {
//        try{
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            String userId = authentication.getName();
//            UserDto userDto = userService.getOneUser(userId);
//        } catch () {
//
//        }
//    }


}
