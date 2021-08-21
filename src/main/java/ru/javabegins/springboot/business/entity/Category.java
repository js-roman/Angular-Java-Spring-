package ru.javabegins.springboot.business.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.javabegins.springboot.auth.entity.User;

import javax.persistence.*;
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Getter
@Setter

public class Category {

    // указываем, что поле заполняется в БД
    // нужно, когда добавляем новый объект и он возвращается уже с новым id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(name = "title", nullable = false, length = -1)
    private String title;


    @Column(name = "completed_count", nullable = true)
    private Long completedCount;


    @Column(name = "uncompleted_count", nullable = true)
    private Long uncompletedCount;

//    @Basic
//    @Column(name = "user_id", nullable = false)
//    private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

//    @OneToMany(mappedBy = "categoryByCategoryId")
//    private Collection<Task> tasksById;



}
