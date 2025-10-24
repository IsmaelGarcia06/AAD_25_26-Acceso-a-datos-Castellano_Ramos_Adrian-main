package com.example.demo;


import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class leerAlumnoDesdeCSV {

    // Método estático que lee un archivo CSV y devuelve una lista de objetos Alumno
    public static List<Alumno> leerAlumnos(String ruta) {

        // Creo una lista vacía donde iré guardando los alumnos que lea del fichero
        List<Alumno> alumnos = new ArrayList<>();


        // Hago un try-catch para asegurarme de cerrar el BufferedReader al finalizar
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {

            String linea = br.readLine();

            // Leo todo el archivo
            while ((linea = br.readLine()) != null) {

                // Divido la linea por las comas para los campos
                String[] partes = linea.split(",");

                // Convierto cada parte a su tipo correspondiente
                int id = Integer.parseInt(partes[0]);
                String nombre = partes[1];
                double nota = Double.parseDouble(partes[2]);

                // Creo un nuevo objeto Alumno con los datos leídos
                Alumno a = new Alumno(id, nombre, nota);

                // Añado el alumno a la lista
                alumnos.add(a);
            }

            
        } catch (IOException e) {
            log.info("Error al leer los alumnos del fichero CSV");
            e.printStackTrace();
        }

        // Devuelvo la lista de alumnos ya cargada desde el CSV
        return alumnos;
    }
}
