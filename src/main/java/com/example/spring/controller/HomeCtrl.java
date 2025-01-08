package com.example.spring.controller;

//테스트 위한 임시 홈컨트롤러임

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeCtrl {
    @Autowired
    @GetMapping("/")
    public String home(){
        return "home";
    }
}
