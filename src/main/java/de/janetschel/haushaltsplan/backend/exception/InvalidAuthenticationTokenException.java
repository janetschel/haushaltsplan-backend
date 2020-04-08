package de.janetschel.haushaltsplan.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Request rejected due to invalid auth-token")
public class InvalidAuthenticationTokenException extends Exception {
    public InvalidAuthenticationTokenException() {
        super();
    }
}
