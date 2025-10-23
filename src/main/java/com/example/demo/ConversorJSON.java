package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class ConversorJSON {

    public static void escribirJSON(List<Alumno> alumnos, String rutaSalida) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(rutaSalida), alumnos);

            log.info("Archivo JSON creado con éxito: {}", rutaSalida);
        } catch (IOException e) {
            log.error("Error al escribir el archivo JSON: {}", e.getMessage());
        }
    }
}
