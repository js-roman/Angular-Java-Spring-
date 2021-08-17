package ru.javabegins.springboot.business.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter

public class Stat {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "completed_total", nullable = false)
    private Long completedTotal;

    @Basic
    @Column(name = "uncompleted_total", nullable = false)
    private Long uncompletedTotal;

    @Basic
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserData userDataByUserId;


}
