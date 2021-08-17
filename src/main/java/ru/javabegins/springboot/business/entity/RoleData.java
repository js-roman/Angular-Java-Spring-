package ru.javabegins.springboot.business.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter

@Table(name = "role_data", schema = "tasklist", catalog = "postgres")
public class RoleData {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    private String name;

    @OneToMany(mappedBy = "roleDataByRoleId")
    private Collection<UserRole> userRolesById;


}
