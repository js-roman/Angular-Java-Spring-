package ru.javabegins.springboot.business.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.javabegins.springboot.auth.entity.User;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter

public class Stat {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "completed_total", nullable = false)
    private Long completedTotal;

    @Column(name = "uncompleted_total", nullable = false)
    private Long uncompletedTotal;

//    @Basic
//    @Column(name = "user_id", nullable = false)
//    private Long user;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;


}
