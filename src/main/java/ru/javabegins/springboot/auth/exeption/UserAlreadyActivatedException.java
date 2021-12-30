package ru.javabegins.springboot.auth.exeption;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyActivatedException extends AuthenticationException {
    public UserAlreadyActivatedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserAlreadyActivatedException(String msg) {
        super(msg);
    }
}
