package com.heroku.backend.service;

import com.heroku.backend.config.ValueConfig;
import com.heroku.backend.exception.InvalidBase64StringException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenService {

    public ResponseEntity<String> getAuthToken(String base64String) throws InvalidBase64StringException {
        if (base64String.equals(ValueConfig.base64FirstUser) ||
                base64String.equals(ValueConfig.base64SecondUser)) {
            return ResponseEntity.ok(ValueConfig.authenticationToken);
        }

        throw new InvalidBase64StringException();
    }
}
