package com.springboot.jpa.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Mission {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int period;
    @ManyToMany(mappedBy = "missions")
    private List<Employee> employees;
}
