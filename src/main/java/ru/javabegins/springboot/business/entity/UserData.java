package ru.javabegins.springboot.business.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "user_data", schema = "tasklist", catalog = "postgres")
public class UserData {
    private Long id;
    private String email;
    private String password;
    private String username;
    private Collection<Activity> activitiesById;
    private Collection<Category> categoriesById;
    private Collection<Priority> prioritiesById;
    private Collection<Stat> statsById;
    private Collection<Task> tasksById;
    private UserRole userRoleById;

    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return Objects.equals(id, userData.id) && Objects.equals(email, userData.email) && Objects.equals(password, userData.password) && Objects.equals(username, userData.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, username);
    }

    @OneToMany(mappedBy = "userDataByUserId")
    public Collection<Activity> getActivitiesById() {
        return activitiesById;
    }

    public void setActivitiesById(Collection<Activity> activitiesById) {
        this.activitiesById = activitiesById;
    }

    @OneToMany(mappedBy = "userDataByUserId")
    public Collection<Category> getCategoriesById() {
        return categoriesById;
    }

    public void setCategoriesById(Collection<Category> categoriesById) {
        this.categoriesById = categoriesById;
    }

    @OneToMany(mappedBy = "userDataByUserId")
    public Collection<Priority> getPrioritiesById() {
        return prioritiesById;
    }

    public void setPrioritiesById(Collection<Priority> prioritiesById) {
        this.prioritiesById = prioritiesById;
    }

    @OneToMany(mappedBy = "userDataByUserId")
    public Collection<Stat> getStatsById() {
        return statsById;
    }

    public void setStatsById(Collection<Stat> statsById) {
        this.statsById = statsById;
    }

    @OneToMany(mappedBy = "userDataByUserId")
    public Collection<Task> getTasksById() {
        return tasksById;
    }

    public void setTasksById(Collection<Task> tasksById) {
        this.tasksById = tasksById;
    }

    @OneToOne(mappedBy = "userDataByUserId")
    public UserRole getUserRoleById() {
        return userRoleById;
    }

    public void setUserRoleById(UserRole userRoleById) {
        this.userRoleById = userRoleById;
    }
}
