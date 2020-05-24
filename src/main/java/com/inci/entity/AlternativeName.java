package com.inci.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "alternative_name")
public class AlternativeName {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "alternameid")
    private Long alternativeNameId;

    @ManyToOne
    @JoinColumn(name = "inciid")
    private Inci inci;

    @Column(name = "altname")
    private String alternativeName;
}