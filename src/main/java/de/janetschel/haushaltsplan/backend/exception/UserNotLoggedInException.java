package de.janetschel.haushaltsplan.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "User not yet logged in")
public class UserNotLoggedInException extends Exception{
    public UserNotLoggedInException() {
        super();
    }
}
