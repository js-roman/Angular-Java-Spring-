package ru.javabegins.springboot.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.javabegins.springboot.auth.entity.Role;

import java.util.Optional;

@Repository
public interface  RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String rolename);
}
