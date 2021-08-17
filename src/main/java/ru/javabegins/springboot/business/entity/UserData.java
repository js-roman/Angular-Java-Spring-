package ru.javabegins.springboot.business.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "user_data", schema = "tasklist", catalog = "postgres")
@Setter
@Getter
public class UserData {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "email", nullable = false, length = -1)
    private String email;

    @Basic
    @Column(name = "password", nullable = false, length = -1)
    private String password;


    @Basic
    @Column(name = "username", nullable = false, length = -1)
    private String username;

    @OneToMany(mappedBy = "userDataByUserId")
    private Collection<Activity> activitiesById;

    @OneToMany(mappedBy = "userDataByUserId")
    private Collection<Category> categoriesById;

    @OneToMany(mappedBy = "userDataByUserId")
    private Collection<Priority> prioritiesById;

    @OneToMany(mappedBy = "userDataByUserId")
    private Collection<Stat> statsById;

    @OneToMany(mappedBy = "userDataByUserId")
    private Collection<Task> tasksById;

    @OneToMany(mappedBy = "userDataByUserId")
    private Collection<UserRole> userRolesById;



}
