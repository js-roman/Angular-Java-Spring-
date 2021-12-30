package ru.javabegins.springboot.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.SessionManagementFilter;
import ru.javabegins.springboot.auth.filter.AuthTokenFilter;
import ru.javabegins.springboot.auth.filter.ExceptionHandlerFilter;
import ru.javabegins.springboot.auth.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableAsync
public class SpringConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;
    private AuthTokenFilter authTokenFilter;
    private ExceptionHandlerFilter exceptionHandlerFilter;

    @Autowired
    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setAuthTokenFilter(AuthTokenFilter authTokenFilter) {
        this.authTokenFilter = authTokenFilter;
    }

    @Autowired
    public void setExceptionHandlerFilter(ExceptionHandlerFilter exceptionHandlerFilter) {
        this.exceptionHandlerFilter = exceptionHandlerFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public FilterRegistrationBean registration(AuthTokenFilter authTokenFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean((authTokenFilter));
        registration.setEnabled(false);
        return registration;
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.
                userDetailsService(userDetailsService).
                passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // порядок следования настроек внутри метода - неважен



        /* если используется другая клиентская технология (не SpringMVC, а например Angular, React и пр.),
            то выключаем встроенную Spring-защиту от CSRF атак,
            иначе запросы от клиента не будут обрабатываться, т.к. Spring Security будет пытаться в каждом входящем запроcе искать спец. токен для защиты от CSRF
        */
        http.csrf().disable(); // на время разработки проекта не будет ошибок (для POST, PUT и др. запросов) - недоступен и т.д.


        http.formLogin().disable(); // отключаем, т.к. форма авторизации создается не на Spring технологии (например, Spring MVC + JSP), а на любой другой клиентской технологии
        http.httpBasic().disable(); // отключаем стандартную браузерную форму авторизации

        http.requiresChannel().anyRequest().requiresSecure(); // обязательное исп. HTTPS
        http.addFilterBefore(authTokenFilter, SessionManagementFilter.class);
        http.addFilterBefore(exceptionHandlerFilter, AuthTokenFilter.class);

    }
}
