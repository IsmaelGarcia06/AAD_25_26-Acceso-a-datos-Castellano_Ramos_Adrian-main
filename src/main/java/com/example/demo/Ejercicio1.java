package com.example.demo;

import org.springframework.boot.SpringApplication;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Ejercicio1 {
    public static void main(String[] args) throws IOException {
        //esto es el menu tampoco hay mucho que explicar, leelo

        boolean salir = false;
       Scanner sc = new Scanner(System.in);
        System.out.println("Introduce la ruta del directorio:");
        String directorio = sc.nextLine();
        Path dir = Paths.get(directorio);

        if (!Files.exists(dir)) {
            System.out.println("Directorio no encontrado");
            return;
        }
        if (!Files.isDirectory(dir)) {
            System.out.println("No es un directorio");
            return;
        }
        while (!salir) {
            listarDirectorios(dir);
            System.out.println("Menú:");
            System.out.println("1) Crear fichero");
            System.out.println("2) Mover fichero");
            System.out.println("3) Borrar fichero");
            System.out.println("4) Cambiar directorio");
            System.out.println("0) Salir");
            System.out.print("Elige: ");
            String opt = sc.nextLine().trim();
            switch (opt) {
                case "1": crearFicheroVacio(dir, sc); break;
                case "2": moverFichero(dir, sc); break;
                case "3": borrarFichero(dir, sc); break;
                case "4": System.out.print("Nueva ruta: "); dir = Paths.get(sc.nextLine()).toAbsolutePath().normalize(); break;
                case "0": salir = true; break;
                default: System.out.println("Opción no válida");
            }
        }
       System.out.println("Directorio existente" + dir);
       sc.close();
    }

    private static void listarDirectorios(Path dir) {
        // Formato para mostrar la fecha bonita (día/mes/año hora:minuto:segundo)
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir)) {
            for (Path path : ds) {
                // Obtenemos los atributos básicos del archivo o directorio
                BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);

                String nombre = path.getFileName().toString(); // nombre del fichero o carpeta
                long tamanyo = attrs.size();  // tamaño en bytes
                String fecha = sdf.format(attrs.lastModifiedTime().toMillis()); // última modificación

                // Diferenciamos si es fichero o directorio
                if (attrs.isDirectory()) {
                    System.out.println("[Directorio]  " + nombre + " | " + tamanyo + " bytes | " + fecha);
                } else {
                    System.out.println("[Archivo] " + nombre + " | " + tamanyo + " bytes | " + fecha);
                }
            }
        } catch (IOException e) {
            System.out.println("Error listando directorio: " + e.getMessage());
        }
    }

    private static Path resolverRutaContraDir(Path dirActual, String entrada) {
        Path p = Paths.get(entrada);   // convertir la cadena a Path
        if (p.isAbsolute()) {         // si es absoluta, usarla tal cual
            return p.normalize().toAbsolutePath();
        } else {       // si es relativa, resolverla contra dirActual
            return dirActual.resolve(p).normalize().toAbsolutePath();
        }
    }
    private static void crearFicheroVacio(Path dirActual, Scanner sc) {
        System.out.print("Introduce el nombre del nuevo fichero: ");
        String nombre = sc.nextLine().trim();

        if (nombre.isEmpty()) {
            System.out.println("No has escrito nada, operación cancelada.");
            return;
        }

        // Resolvemos la ruta: si es relativa, la juntamos con el directorio actual
        Path destino = resolverRutaContraDir(dirActual, nombre);

        try {
            // Si el fichero ya existe, avisamos
            if (Files.exists(destino)) {
                System.out.println("El fichero ya existe: " + destino);
                return;
            }

            // Si el padre (carpeta) no existe, lo creamos
            if (destino.getParent() != null) {
                Files.createDirectories(destino.getParent());
            }

            // Creamos el fichero vacío
            Files.createFile(destino);
            System.out.println("Fichero creado: " + destino);

        } catch (AccessDeniedException ade) {
            System.out.println("No tienes permisos para crear el fichero aquí.");
        } catch (IOException e) {
            System.out.println("Error al crear el fichero: " + e.getMessage());
        }
    }
    private static void moverFichero(Path dirActual, Scanner sc) {
        System.out.print("Nombre del fichero a mover: ");
        String origenTexto = sc.nextLine().trim();
        if (origenTexto.isEmpty()) {
            System.out.println("Operación cancelada.");
            return;
        }

        Path origen = resolverRutaContraDir(dirActual, origenTexto);

        // Comprobamos que exista y que no sea directorio
        if (!Files.exists(origen)) {
            System.out.println("No existe el fichero: " + origen);
            return;
        }
        if (Files.isDirectory(origen)) {
            System.out.println("Lo indicado es un directorio. Solo se pueden mover ficheros.");
            return;
        }

        // Pedimos destino
        System.out.print("Introduce la ruta de destino: ");
        String destinoTexto = sc.nextLine().trim();
        if (destinoTexto.isEmpty()) {
            System.out.println("Operación cancelada.");
            return;
        }

        Path destino = resolverRutaContraDir(dirActual, destinoTexto);

        try {
            // Si el destino es un directorio existente, colocamos el fichero dentro
            if (Files.exists(destino) && Files.isDirectory(destino)) {
                destino = destino.resolve(origen.getFileName());
            }

            // Si el destino ya existe, pedimos confirmación
            if (Files.exists(destino)) {
                System.out.print("El fichero ya existe en el destino. ¿Sobrescribir? (s/n): ");
                String respuesta = sc.nextLine().trim().toLowerCase();
                if (!respuesta.equals("s")) {
                    System.out.println("Operación cancelada.");
                    return;
                }
                Files.move(origen, destino, StandardCopyOption.REPLACE_EXISTING);
            } else {
                // Si no existe, lo movemos normalmente
                if (destino.getParent() != null) {
                    Files.createDirectories(destino.getParent());
                }
                Files.move(origen, destino);
            }

            System.out.println("Fichero movido a: " + destino);

        } catch (AccessDeniedException ade) {
            System.out.println("No tienes permisos para mover este fichero.");
        } catch (IOException e) {
            System.out.println("Error al mover fichero: " + e.getMessage());
        }
    }
    private static void borrarFichero(Path dirActual, Scanner sc) {
        System.out.print("Nombre del fichero a borrar: ");
        String entrada = sc.nextLine().trim();
        if (entrada.isEmpty()) {
            System.out.println("Operación cancelada.");
            return;
        }

        Path objetivo = resolverRutaContraDir(dirActual, entrada);

        if (!Files.exists(objetivo)) {
            System.out.println("El fichero no existe: " + objetivo);
            return;
        }
        if (Files.isDirectory(objetivo)) {
            System.out.println("Eso es un directorio. Esta opción solo borra ficheros.");
            return;
        }

        System.out.print("¿Seguro que quieres borrar " + objetivo.getFileName() + "? (s/n): ");
        String respuesta = sc.nextLine().trim().toLowerCase();
        if (!respuesta.equals("s")) {
            System.out.println("Operación cancelada.");
            return;
        }

        try {
            Files.delete(objetivo);
            System.out.println("Fichero borrado: " + objetivo);
        } catch (AccessDeniedException ade) {
            System.out.println("No tienes permisos para borrar este fichero.");
        } catch (IOException e) {
            System.out.println("Error al borrar fichero: " + e.getMessage());
        }
    }
}


