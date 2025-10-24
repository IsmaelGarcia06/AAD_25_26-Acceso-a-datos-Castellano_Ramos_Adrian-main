package com.example.demo;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ConversorXML {
    public static void escribirXML(List<Alumno> alumnos, String ruta) {
        //Y aqui es lo mismo que en JSON pero usando el XmlMapper
        XmlMapper xmlMapper = new XmlMapper();
        try {
            xmlMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new java.io.File(ruta), alumnos);
            log.info("Archivo XML creado con éxito: " + ruta);
        } catch (Exception e) {
            log.info("Error al escribir el archivo XML: " + e.getMessage());
        }

    }
}
