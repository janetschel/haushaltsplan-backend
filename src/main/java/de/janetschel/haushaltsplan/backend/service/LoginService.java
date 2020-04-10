package de.janetschel.haushaltsplan.backend.service;

import de.janetschel.haushaltsplan.backend.App;
import de.janetschel.haushaltsplan.backend.config.ValueConfig;
import de.janetschel.haushaltsplan.backend.exception.TooManyRequestsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class LoginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

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
        LOGGER.info("Reset attempts for login. Accepting all requests now.");

        if (attempts >= 10) {
            LOGGER.warn(attempts + " attempts made since the last reset.");
        } else {
            LOGGER.info(attempts + " attempts made since the last reset. No indication of any attacks.");
        }

        attempts = 0;
    }
}
