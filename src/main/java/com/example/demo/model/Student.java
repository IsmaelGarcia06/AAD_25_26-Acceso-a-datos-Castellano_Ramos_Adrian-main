package com.example.demo.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;


@Data
@ToString
public class Student extends Persona {

    private String curso;
    private List<Module> modules;

    public Student(String dni, String surname, String name) {
        super(dni, surname, name);
    }

    public Student(String dni, String surname, String name, String curso) {
        super(dni, surname, name);
        this.curso = curso;
    }
}
