package com.example.demo;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class leerAlumnoDesdeCSV {
    public static List<Alumno> leerAlumnos(String ruta) {
        List<Alumno> alumnos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea = br.readLine();
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                Alumno alumno = new Alumno();
                int id = Integer.parseInt(partes[0]);
                String nombre = (partes[1]);
                double nota = Double.parseDouble(partes[2]);
                Alumno a = new Alumno(id, nombre, nota);
                alumnos.add(a);
            }

        } catch (IOException e) {
            log.info("Error al leer al alumnos de datos");
            e.printStackTrace();

        }
        return alumnos;
    }
}