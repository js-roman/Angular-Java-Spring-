package ru.javabegins.springboot.auth.controller;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.javabegins.springboot.auth.entity.Activity;
import ru.javabegins.springboot.auth.entity.Role;
import ru.javabegins.springboot.auth.entity.User;
import ru.javabegins.springboot.auth.exeption.RoleNotFoundException;
import ru.javabegins.springboot.auth.exeption.UserAlreadyActivatedException;
import ru.javabegins.springboot.auth.exeption.UserOrEmailExistsException;
import ru.javabegins.springboot.auth.objects.JsonException;
import ru.javabegins.springboot.auth.service.*;
import ru.javabegins.springboot.util.CookieUtils;
import ru.javabegins.springboot.util.JwtUtils;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@Log
public class AuthController {
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder encoder;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private CookieUtils cookieUtils;
    private EmailService emailService;
    private UserDetailsServiceImpl userDetailsService;
    public static final String DEFAULT_ROLE = "USER";


    @Autowired
    public AuthController(UserService userService, RoleService roleService, PasswordEncoder encoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, CookieUtils cookieUtils, EmailService emailService, UserDetailsServiceImpl userDetailsService) {
        this.userService = userService;
        this.roleService = roleService;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.cookieUtils = cookieUtils;
        this.emailService = emailService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    @PostMapping("/test-no-auth")
    public String testNoAuth() {
        return "OK-no-auth";
    }

    @PostMapping("/test-with-auth")
    @PreAuthorize("hasAuthority('USER')")
    public String testWithAuth() {
        return "OK-with-auth";
    }


    @PutMapping("/register")
    public ResponseEntity register(@Valid @RequestBody User user) {
        if (userService.isUserExists(user.getUsername(), user.getEmail())) {
            throw new UserOrEmailExistsException("User or email already exists");
        }
        Role userRole = roleService.findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new RoleNotFoundException("Default role not found"));
        user.getRoles().add(userRole);

        user.setPassword(encoder.encode(user.getPassword()));

        Activity activity = new Activity();
        activity.setUser(user);
        activity.setUuid(UUID.randomUUID().toString());
        userService.save(user, activity);

        emailService.sendActivationEmail(user.getEmail(), user.getUsername(), activity.getUuid());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/activate-account")
    public ResponseEntity<Boolean> activateUser(@RequestBody String uuid) {
        Activity activity = userService.findActivityByUuid(uuid)
                .orElseThrow(() -> new UsernameNotFoundException("User with activity UUID not found: " + uuid));
        if (activity.isActivated()) {
            throw new UserAlreadyActivatedException("User already activated");
        }
        int updatedCount = userService.activate(uuid);
        return ResponseEntity.ok(updatedCount == 1);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@Valid @RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (!userDetails.isActivated()) {
            throw new DisabledException("User disabled");
        }


        String jwt = jwtUtils.createAccessToken(userDetails.getUser());

        userDetails.getUser().setPassword(null);
        HttpCookie cookie = cookieUtils.createJwtCookie(jwt);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().headers(responseHeaders).body(userDetails.getUser());
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity logout() {
        HttpCookie cookie = cookieUtils.deleteJwtCookie();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @PostMapping("/update-password")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity updatePassword(@RequestBody String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        int updated = userService.updatePassword(user.getUsername(), encoder.encode(password));
        return ResponseEntity.ok(updated == 1);
    }

    @PostMapping("/resend-activate-email")
    public ResponseEntity resendActivateEmail(@RequestBody String usernameOrEmail) {
        UserDetailsImpl user = (UserDetailsImpl) userDetailsService.loadUserByUsername(usernameOrEmail);
        Activity activity = userService.findActivityByUserId(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Activity Not Found with user: " + usernameOrEmail));
        if (activity.isActivated())
            throw new UserAlreadyActivatedException("User already activated: " + usernameOrEmail);
        emailService.sendActivationEmail(user.getEmail(), user.getUsername(), activity.getUuid());
        return ResponseEntity.ok().build();
    }


    @PostMapping("/send-reset-password-email")
    public ResponseEntity sendEmailResetPassword(@RequestBody String email) {

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(email);
        User user = userDetails.getUser();
        if (userDetails != null) {
            emailService.sendResetPasswordEmail(user.getEmail(), jwtUtils.createEmailResetToken(user));
        }
        return ResponseEntity.ok().build();
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonException> handleExceptions(Exception e) {
        return new ResponseEntity(new JsonException(e.getClass().getSimpleName()), HttpStatus.BAD_REQUEST);
    }
}
