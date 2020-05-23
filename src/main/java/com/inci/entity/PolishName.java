package com.inci.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "polish_name")
public class PolishName {

    @Id
    @Column(name = "plnameid")
    private Long polishNameId;

    @ManyToOne
    @JoinColumn(name = "inciid")
    private Inci inci;

    @Column(name = "pl_name")
    private String polishName;

}