package ru.javabegins.springboot.auth.exeption;

import org.springframework.security.core.AuthenticationException;

public class RoleNotFoundException extends AuthenticationException {
    public RoleNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RoleNotFoundException(String msg) {
        super(msg);
    }
}
