package de.janetschel.haushaltsplan.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Request rejected due to invalid bae64 login string")
public class InvalidBase64StringException extends Exception {
    public InvalidBase64StringException() {
        super();
    }
}
