package com.heroku.backend.service;

import com.heroku.backend.ValueConfiguration;
import com.heroku.backend.exception.InvalidBase64StringException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenService {

    public ResponseEntity<String> getAuthToken(String base64String) throws InvalidBase64StringException {
        if (base64String.equals(ValueConfiguration.base64FirstUser) ||
                base64String.equals(ValueConfiguration.base64SecondUser)) {
            return ResponseEntity.ok(ValueConfiguration.authenticationToken);
        }

        throw new InvalidBase64StringException();
    }
}
