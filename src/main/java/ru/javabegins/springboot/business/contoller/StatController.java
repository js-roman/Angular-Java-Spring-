package ru.javabegins.springboot.business.contoller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.javabegins.springboot.business.entity.Stat;
import ru.javabegins.springboot.business.service.StatService;

@RestController
public class StatController {
    private StatService statService;

    @Autowired
    public StatController(StatService statService) {
        this.statService = statService;
    }

    @PostMapping("/stat")
    Stat findByUserEmail(@RequestBody String email){
        return statService.findByUserEmail(email);
    }
}
