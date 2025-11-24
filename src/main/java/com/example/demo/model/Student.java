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
    private Integer id;        // era 'dni'
    private String nif;        // campo del PDF
    private String name;
    private String email;
    private String curse;      // campo del PDF (curso: DAW, DAM, etc.)
    private List<Module> modules;  // relación con módulos

    // Constructor vacío, constructor completo, getters y setters
}