package ru.javabegins.springboot.auth.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.javabegins.springboot.auth.entity.User;
import ru.javabegins.springboot.auth.exeption.JwtCommonException;
import ru.javabegins.springboot.auth.service.UserDetailsImpl;
import ru.javabegins.springboot.util.CookieUtils;
import ru.javabegins.springboot.util.JwtUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";

    private JwtUtils jwtUtils;
    private CookieUtils cookieUtils;

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Autowired
    public void setCookieUtils(CookieUtils cookieUtils) {
        this.cookieUtils = cookieUtils;
    }

    private List<String> permitURL = Arrays.asList(
            "register",
            "login",
            "activate-account",
            "resend-activate-email",
            "send-reset-password-email",
            "test-no-auth",
            "index"
    );


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        boolean isRequestToPublicAPI = permitURL.stream().anyMatch(s -> request.getRequestURI().toLowerCase().contains(s));
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (
                !isRequestToPublicAPI
//                        &&
//                        SecurityContextHolder.getContext().getAuthentication() == null
        ) {


            String jwt = null;
            if (request.getRequestURI().contains("update-password")) {
                jwt = getJwtFromHeader(request);
            } else { // для всех остальных запросов
                jwt = cookieUtils.getCookieAccessToken(request);
            }


            if (jwt != null) {
                User user = jwtUtils.getUser(jwt);
                UserDetails userDetails = new UserDetailsImpl(user);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                if (jwtUtils.validate(jwt)) {
                    System.out.println("jwt = " + jwt);
                } else {
                    throw new JwtCommonException("jwt validate exception");
                }
            } else {
                throw new AuthenticationCredentialsNotFoundException("token not found");
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromHeader(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER_PREFIX)) {
            return headerAuth.substring(7);
        }

        return null;
    }

}
