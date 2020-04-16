package de.janetschel.haushaltsplan.backend.service;

import de.janetschel.haushaltsplan.backend.config.ValueConfig;
import de.janetschel.haushaltsplan.backend.exception.InvalidBase64StringException;
import de.janetschel.haushaltsplan.backend.exception.LoginExpiredException;
import de.janetschel.haushaltsplan.backend.exception.UserNotLoggedInException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenService {

    public ResponseEntity<String> getAuthToken(String base64String)
            throws InvalidBase64StringException, LoginExpiredException, UserNotLoggedInException {

        LoginService.checkIfLoginIsExpired();

        if (!base64String.equals(ValueConfig.base64FirstUser) && !base64String.equals(ValueConfig.base64SecondUser)) {
            throw new InvalidBase64StringException();
        }

        return ResponseEntity.ok(ValueConfig.authenticationToken);
    }
}
