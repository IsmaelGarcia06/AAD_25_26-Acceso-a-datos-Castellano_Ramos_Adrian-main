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
public class Student {
    private Integer id;
    private String nif;
    private String name;
    private String email;
    private String curse;
    private List<Module> modules;


}