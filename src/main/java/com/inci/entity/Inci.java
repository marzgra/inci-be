package com.inci.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "inci")
public class Inci {

    @Id
    @Column(name = "inciid")
    private Long inciId;

    @Column(name = "inciname")
    private String inciName;

    @Column(name = "description")
    private String description;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "inci_function",
            joinColumns = @JoinColumn(name = "inciid"),
            inverseJoinColumns = @JoinColumn(name = "functionid")
    )
    private List<Function> functions = new ArrayList<>();

    @OneToMany(mappedBy = "inci")
    private List<PolishName> polishNames = new ArrayList<>();

    @OneToMany(mappedBy = "inci")
    private List<AlternativeName> alternativeNames = new ArrayList<>();

}