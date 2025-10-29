package com.example.demo.service;

import com.example.demo.model.LogEntry;
import com.example.demo.repository.LogRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class LogService {
    private LogRepository repositorio;

    public LogService(LogRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void añadirEvento(String mensaje) {
        LogEntry evento = new LogEntry(mensaje);
        repositorio.guardarEvento(evento);
        log.info("Evento guardado correctamente.");
    }

    public void mostrarEventosPorFecha(String fecha) { //recorre to los evento y pilla solo los de la fehca que has pasao
        List<LogEntry> eventos = repositorio.leerEventos();
        boolean encontrado = false;
        for (LogEntry e : eventos) {
            if (e.getFechaHora().toString().startsWith(fecha)) {
                log.info(e.formatear());
                encontrado = true;
            }
        }
        if (!encontrado) {
            log.info("No hay eventos registrados para esa fecha.");
        }
    }

    public void cambiarCodificacion(String nuevaCodificacion) {
        repositorio.cambiarCodificacion(nuevaCodificacion);
    }
}
