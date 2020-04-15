package de.janetschel.haushaltsplan.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Login expired. Please log in again")
public class LoginExpiredException extends Exception {
    public LoginExpiredException() {
        super();
    }
}
