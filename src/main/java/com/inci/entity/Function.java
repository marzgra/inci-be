package com.inci.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "function")
public class Function {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "functionid")
    private Long functionId;

    @Column(name = "function_name")
    private String functionName;

    @ManyToMany(mappedBy = "functions", cascade =
            {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Inci> incis;
}