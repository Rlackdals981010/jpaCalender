package com.kcm.jpacalender.controller;


import com.kcm.jpacalender.dto.LoginRequestDto;
import com.kcm.jpacalender.dto.UserRequestDto;
import com.kcm.jpacalender.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private  final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public void logIn(@RequestBody UserRequestDto loginRequestDto, HttpServletResponse res){
        userService.logIn(loginRequestDto,res);
    }
}