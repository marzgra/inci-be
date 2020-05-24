package com.inci.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "polish_name")
public class PolishName {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "plnameid")
    private Long polishNameId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inciid", referencedColumnName = "inciid")
    private Inci inci;

    @Column(name = "pl_name")
    private String polishName;

}