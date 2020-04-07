package com.heroku.backend.service;

import com.heroku.backend.config.ValueConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    public ResponseEntity<Boolean> loginUser(String base64String) {
        if (base64String.equals(ValueConfig.base64FirstUser) ||
                base64String.equals(ValueConfig.base64SecondUser)) {
            return ResponseEntity.ok(true);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
    }
}
