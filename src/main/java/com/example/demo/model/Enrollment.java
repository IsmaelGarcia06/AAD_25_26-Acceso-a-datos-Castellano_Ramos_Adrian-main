package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "matricula")
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_alumno")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "id_modulo")
    private Module module;

    private LocalDate enrollmentDate;
    private Double finalGrade; // Atributo adicional solicitado [cite: 64]
}