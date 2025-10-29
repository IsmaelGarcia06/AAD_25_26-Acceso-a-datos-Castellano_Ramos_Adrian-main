package com.example.demo.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogEntry {
    private LocalDateTime fechaHora; //Esto pa guarda la hora y la fecha
    private String mensaje; // y esto pa guarda el texto del evento

    public LogEntry(String mensaje) {
        this.fechaHora = LocalDateTime.now(); //Esto pa lo primero
        this.mensaje = mensaje; //y esto pa lo segundo
    }

    public LogEntry(LocalDateTime fechaHora, String mensaje) {
        this.fechaHora = fechaHora;
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String formatear() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] %s", fechaHora.format(formatter), mensaje);
    }
}

