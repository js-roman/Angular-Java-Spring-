package ru.javabegins.springboot.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javabegins.springboot.auth.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserById(Long id);

    @Query("SELECT case when count(u)>0 then true else false end FROM User u WHERE lower(u.email)=lower(:email)")
    boolean existsEmail(@Param("email") String email);

    @Query("SELECT case when count(u)>0 then true else false end FROM User u WHERE lower(u.username)=lower(:username)")
    boolean existsUsername(@Param("username") String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String username);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password=:password WHERE u.username=:username")
    int updatePassword(@Param("username") String username, @Param("password") String password);
}
