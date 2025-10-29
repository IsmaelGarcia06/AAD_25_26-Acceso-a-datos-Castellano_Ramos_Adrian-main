package com.example.demo;

import com.example.demo.repository.LogRepository;
import com.example.demo.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@Slf4j
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner sc = new Scanner(System.in);
        LogRepository repo = new LogRepository("src/main/resources/app.log", "UTF-8");
        LogService servicio = new LogService(repo);

        int opcion;
        do {
            log.info("\n=== GESTOR DE LOGS ===");
            log.info("1. Añadir evento");
            log.info("2. Filtrar por fecha");
            log.info("3. Cambiar codificación");
            log.info("4. Salir");
            log.info("Opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> {
                    log.info("Mensaje del evento: ");
                    String mensaje = sc.nextLine();
                    servicio.añadirEvento(mensaje);
                }
                case 2 -> {
                    log.info("Introduce la fecha (YYYY-MM-DD): ");
                    String fecha = sc.nextLine();
                    servicio.mostrarEventosPorFecha(fecha);
                }
                case 3 -> {
                    log.info("Nueva codificación (UTF-8 o ISO-8859-1): ");
                    String enc = sc.nextLine();
                    servicio.cambiarCodificacion(enc);
                }
                case 4 -> log.info("Saliendo...");
                default -> log.info("Opción inválida.");
            }
        } while (opcion != 4);
    }
}
