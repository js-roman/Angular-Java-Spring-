package ru.javabegins.springboot.auth.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.javabegins.springboot.auth.objects.JsonException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }
        catch (RuntimeException e){
            JsonException je=new JsonException(e.getClass().getSimpleName());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(convertObjectToJson(je));
        }
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException{
        if(object==null) return null;
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
