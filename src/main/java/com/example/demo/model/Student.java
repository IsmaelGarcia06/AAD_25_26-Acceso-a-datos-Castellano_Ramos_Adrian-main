package com.example.demo.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student extends Person {

    private String curso;
    private List<Modules> modules;


    public Student(int dni, String name, String email) {
        super(dni, name, email);
    }

}
