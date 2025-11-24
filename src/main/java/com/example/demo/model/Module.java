package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Module {
    private Integer id;      // ⬅️ AÑADIR - se autogenera en BD
    private String code;
    private String name;
    private Integer hours;   // ⬅️ AÑADIR - requerido por el PDF
}