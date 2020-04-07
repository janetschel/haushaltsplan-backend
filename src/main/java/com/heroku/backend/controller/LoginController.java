package com.heroku.backend.controller;

import com.heroku.backend.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    public final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public ResponseEntity<Boolean> loginUser(@RequestParam("auth") String base64String) {
        return loginService.loginUser(base64String);
    }
}
