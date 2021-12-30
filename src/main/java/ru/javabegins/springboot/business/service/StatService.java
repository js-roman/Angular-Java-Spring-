package ru.javabegins.springboot.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javabegins.springboot.business.entity.Stat;
import ru.javabegins.springboot.business.repository.StatRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class StatService {
    private StatRepository statRepository;

    @Autowired
    public StatService(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    public Stat findByUserEmail(String email){
        return statRepository.findByUserEmail(email);
    }
}
