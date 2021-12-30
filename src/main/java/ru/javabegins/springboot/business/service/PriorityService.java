package ru.javabegins.springboot.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javabegins.springboot.business.entity.Priority;
import ru.javabegins.springboot.business.repository.PriorityRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PriorityService {
    private PriorityRepository priorityRepository;

    @Autowired
    public PriorityService(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    public List<Priority> findAll(String email) {
        return priorityRepository.findByUserEmailOrderByTitleAsc(email);
    }

    public Priority add(Priority priority) {
        return priorityRepository.save(priority);
    }

    public void delete(Long id) {
        priorityRepository.deleteById(id);
    }

    public List<Priority> search(String title, String email) {
        return priorityRepository.search(title, email);
    }

    public Priority findById(Long id){
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(priorityRepository.findById(id).get());
        return priorityRepository.findById(id).get();
    }

}
