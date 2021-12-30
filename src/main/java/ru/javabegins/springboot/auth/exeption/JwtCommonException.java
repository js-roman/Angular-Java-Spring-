package ru.javabegins.springboot.auth.exeption;

import org.springframework.security.core.AuthenticationException;

public class JwtCommonException extends AuthenticationException {
    public JwtCommonException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JwtCommonException(String msg) {
        super(msg);
    }
}
