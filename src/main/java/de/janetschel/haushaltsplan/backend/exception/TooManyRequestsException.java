package de.janetschel.haushaltsplan.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.TOO_MANY_REQUESTS, reason = "Request rejected")
public class TooManyRequestsException extends Exception {
    public TooManyRequestsException() {
        super();
    }
}
