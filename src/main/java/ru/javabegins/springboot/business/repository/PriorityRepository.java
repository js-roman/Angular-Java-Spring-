package ru.javabegins.springboot.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.javabegins.springboot.business.entity.Priority;

import java.util.List;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {

    List<Priority> findByUserEmailOrderByTitleAsc(String email);

    @Query("SELECT p FROM Priority p WHERE " +
            " :title is null or :title='' " +
            " or lower(p.title) like lower(concat('%',:title,'%')) " +
            " and p.user.email=:email " +
            " order by p.id ASC"
    )
    List<Priority> search(@Param("title") String title, @Param("email") String email);

}