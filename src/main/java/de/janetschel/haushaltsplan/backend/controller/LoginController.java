package de.janetschel.haushaltsplan.backend.controller;

import de.janetschel.haushaltsplan.backend.exception.TooManyRequestsException;
import de.janetschel.haushaltsplan.backend.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    public final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public ResponseEntity<Boolean> loginUser(@RequestHeader("Auth-String") String base64String) throws TooManyRequestsException {
        return loginService.loginUser(base64String);
    }
}
