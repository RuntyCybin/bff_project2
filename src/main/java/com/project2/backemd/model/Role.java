package com.project2.backemd.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
    
    @Id
    @Column(name = "rol_id")
    private Integer id;

    @Column(name = "descripcion", length = 100)
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
