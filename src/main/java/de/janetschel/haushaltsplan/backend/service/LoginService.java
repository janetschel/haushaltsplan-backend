package de.janetschel.haushaltsplan.backend.service;

import de.janetschel.haushaltsplan.backend.config.ValueConfig;
import de.janetschel.haushaltsplan.backend.exception.TooManyRequestsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class LoginService {

    private static int attempts = 0;

    public ResponseEntity<Boolean> loginUser(String base64String) throws TooManyRequestsException {
        if (++attempts >= 10) {
            throw new TooManyRequestsException();
        }

        if (base64String.equals(ValueConfig.base64FirstUser) ||
                base64String.equals(ValueConfig.base64SecondUser)) {
            return ResponseEntity.ok(true);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
    }

    @Scheduled(cron = "*/20 * * * * *")
    private void resetAttempts() {
        attempts = 0;
    }
}
