package com.socialweb.demo.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "roles")//nombre de la tabla
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)// sta enumerado con un enum d tipo string
    @NaturalId
    @Column(length = 60)
    private RoleNames name;

    public Roles(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public RoleNames getName() {
        return name;
    }

    public void setName(RoleNames name) {
        this.name = name;
    }
}