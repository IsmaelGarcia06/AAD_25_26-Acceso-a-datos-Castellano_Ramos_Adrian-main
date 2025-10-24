package com.example.demo;

public class Alumno {
    private int id;
    private String nombre;
    private double nota;

    public Alumno(int id, String nombre, double nota) {
        this.id = id;
        this.nombre = nombre;
        this.nota = nota;
    }

    public Alumno() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }
}
