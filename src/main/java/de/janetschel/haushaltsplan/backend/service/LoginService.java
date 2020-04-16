package de.janetschel.haushaltsplan.backend.service;

import de.janetschel.haushaltsplan.backend.config.ValueConfig;
import de.janetschel.haushaltsplan.backend.exception.LoginExpiredException;
import de.janetschel.haushaltsplan.backend.exception.TooManyRequestsException;
import de.janetschel.haushaltsplan.backend.exception.UserNotLoggedInException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@EnableScheduling
public class LoginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

    private static int attempts = 0;

    public ResponseEntity<Boolean> loginUser(String base64String) throws TooManyRequestsException {
        if (++attempts >= 10) {
            throw new TooManyRequestsException();
        }

        if (!base64String.equals(ValueConfig.base64FirstUser) && !base64String.equals(ValueConfig.base64SecondUser)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
        }

        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(10);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Valid-Thru", localDateTime.toString());

        System.setProperty("login.valid.thru", localDateTime.toString());

        return ResponseEntity.ok().headers(httpHeaders).body(true);
    }

    public ResponseEntity<String> loginValidThru(String base64String) {
        if (!base64String.equals(ValueConfig.base64FirstUser) && !base64String.equals(ValueConfig.base64SecondUser)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed");
        }

        return ResponseEntity.ok(System.getProperty("login.valid.thru"));
    }

    @Scheduled(cron = "*/20 * * * * *")
    private void resetAttempts() {
        if (attempts >= 10) {
            LOGGER.warn(attempts + " attempts made since the last reset.");
        }

        attempts = 0;
    }

    protected static void checkIfLoginIsExpired() throws LoginExpiredException, UserNotLoggedInException {
        String validThru = System.getProperty("login.valid.thru");

        if (validThru == null) {
            throw new UserNotLoggedInException();
        }

        LocalDateTime loginValidThru = LocalDateTime.parse(validThru);
        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(loginValidThru)) {
            throw new LoginExpiredException();
        }

        renewLogin();
    }

    protected static void renewLogin() {
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(10);
        System.setProperty("login.valid.thru", localDateTime.toString());
    }
}
