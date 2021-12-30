package ru.javabegins.springboot.auth.exeption;

import org.springframework.security.core.AuthenticationException;

public class UserOrEmailExistsException extends AuthenticationException {
    public UserOrEmailExistsException(String msg) {
        super(msg);
    }

    public UserOrEmailExistsException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
