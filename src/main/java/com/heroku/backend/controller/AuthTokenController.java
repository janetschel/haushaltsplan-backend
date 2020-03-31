package com.heroku.backend.controller;

import com.heroku.backend.exception.InvalidBase64StringException;
import com.heroku.backend.service.AuthTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthTokenController {

    public final AuthTokenService authTokenService;

    public AuthTokenController(AuthTokenService authTokenService) {
        this.authTokenService = authTokenService;
    }

    @GetMapping("getAuthToken")
    public ResponseEntity<String> getAuthToken(@RequestHeader("Authorization") String base64String)
            throws InvalidBase64StringException {
        return authTokenService.getAuthToken(base64String);
    }
}
