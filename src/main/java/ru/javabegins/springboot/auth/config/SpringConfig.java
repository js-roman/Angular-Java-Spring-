package ru.javabegins.springboot.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity(debug = true)
public class SpringConfig extends WebSecurityConfigurerAdapter {
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


    }
}
