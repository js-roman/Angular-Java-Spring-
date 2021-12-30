package ru.javabegins.springboot.business.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.javabegins.springboot.business.entity.Stat;

@Repository
public interface StatRepository extends CrudRepository<Stat, Long> {

    Stat findByUserEmail(String email);
}
