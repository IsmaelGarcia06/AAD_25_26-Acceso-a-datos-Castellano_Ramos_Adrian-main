package com.example.demo.repository;

import com.example.demo.model.LogEntry;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LogRepository {
    private String rutaFichero; //ruta donde va a esta el fichero
    private String codificacion; //codificacion actual del fichero

    public LogRepository(String rutaFichero, String codificacion) {
        this.rutaFichero = rutaFichero;
        this.codificacion = codificacion;
    }

    public void guardarEvento(LogEntry evento) { //esto tampoco tiene mucho misterio guarda el evento en el fichero
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(rutaFichero, true), Charset.forName(codificacion)))) {
            writer.write(evento.formatear());
            writer.newLine();
        } catch (IOException e) {
            log.info("Error al guardar el evento: " + e.getMessage());
        }
    }

    public List<LogEntry> leerEventos() { //aqui lee los eventos con el parsearLinea
        List<LogEntry> eventos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(rutaFichero), Charset.forName(codificacion)))) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                eventos.add(parsearLinea(linea));
            }
        } catch (IOException e) {
            log.info("Error al leer los eventos: " + e.getMessage());
        }
        return eventos;
    }

    private LogEntry parsearLinea(String linea) {
        try {
            String fecha = linea.substring(1, 20); // coge la fecha de [YYYY-MM-DD HH:mm:ss]
            String mensaje = linea.substring(22); //esto coge el mensaje
            return new LogEntry(LocalDateTime.parse(fecha, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), mensaje);
        } catch (Exception e) {
            return new LogEntry("Línea inválida: " + linea);
        }
    }

    public void cambiarCodificacion(String nuevaCodificacion) {
        if (!nuevaCodificacion.equalsIgnoreCase("UTF-8") &&
                !nuevaCodificacion.equalsIgnoreCase("ISO-8859-1")) {
            log.info("Codificación no válida. Se mantiene la actual: " + codificacion);
            return;
        }
        this.codificacion = nuevaCodificacion;
        log.info("Codificación cambiada a: " + codificacion);
    }

    public String getCodificacion() {
        return codificacion;
    }
}
