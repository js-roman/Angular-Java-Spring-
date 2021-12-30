package ru.javabegins.springboot.business.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.javabegins.springboot.auth.entity.User;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode

public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "title", nullable = false, length = -1)
    private String title;

    @Basic
    @Column(name = "completed", nullable = true)
    private Short completed;

    @Basic
    @Column(name = "task_date", nullable = true)
    private Date taskDate;

//    @Basic
//    @Column(name = "priority_id", nullable = true)
//    private Long priorityId;
//
//    @Basic
//    @Column(name = "category_id", nullable = true)
//    private Long categoryId;
//
//    @Basic
//    @Column(name = "user_id", nullable = false)
//    private Long userId;

    @ManyToOne
    @JoinColumn(name = "priority_id", referencedColumnName = "id")
    private Priority priority ;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;


}
