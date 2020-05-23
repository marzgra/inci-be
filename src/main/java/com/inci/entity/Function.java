package com.inci.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "function")
public class Function {

    @Id
    @Column(name = "functionid")
    private Long functionId;

    @Column(name = "function_name")
    private String functionName;

    @ManyToMany(mappedBy = "functions")
    private List<Inci> incis;
}