package ru.javabegins.springboot.business.contoller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")

public class TestController {

    @GetMapping("test1")
    public String test(){
        return ("works");
    }
}
