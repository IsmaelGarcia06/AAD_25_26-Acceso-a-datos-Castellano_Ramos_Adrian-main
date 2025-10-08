package com.example.demo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ejercicio2 {
    public static void main(String[] args) {
        //Esto es el menu, no tiene mucho lio
        boolean salir = false;
        Scanner sc = new Scanner(System.in);
        int idAlumno = 0;
        String nombreAlumno = "";
        double notaAlumno = 0;
        while (!salir) {

            System.out.println("Menú:");
            System.out.println("1) Insertar nuevo registro");
            System.out.println("2) Consulta alumno por posicion");
            System.out.println("3) Modifica la nota del alumno");
            System.out.println("0) Salir");
            System.out.print("Elige: ");
            String opt = sc.nextLine().trim();
            switch (opt) {
                case "1": insertarRegistro(idAlumno, nombreAlumno, notaAlumno); break;
                case "2": consultaAlumno(); break;
                case "3": modificarAlumno(); break;
                case "0": salir = true; break;
                default: System.out.println("Opción no válida");
                sc.close();
                break;
            }
        }
    }
    private static void insertarRegistro(int id, String nombre, double nota){
        Scanner sc = new Scanner(System.in);
        String directorio = "C:\\Users\\Ismael\\Desktop\\demo\\Ejercicio2.txt";

        try (FileWriter writer = new FileWriter(directorio, true)) { // Aqui escribo sin sobre escribir los archivos pa no liarla y to en una linea
            System.out.print("Introduce el ID del alumno: ");
            id = Integer.parseInt(sc.nextLine());

            System.out.print("Introduce el nombre del alumno: ");
            nombre = sc.nextLine();

            System.out.print("Introduce la nota del alumno: ");
            nota = Double.parseDouble(sc.nextLine());


            writer.write(id + " - " + nombre + " - " + nota + System.lineSeparator());

            System.out.println("Registro guardado correctamente en el fichero.");

        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error: formato de número incorrecto.");
        }
    }
    private static void consultaAlumno() {
        Scanner sc = new Scanner(System.in);
        String directorio = "C:\\Users\\Ismael\\Desktop\\demo\\Ejercicio2.txt";
        ArrayList<String> lineas = new ArrayList<>();

        // aqui leo el fichero y lo voy metiendo en el arraidlist
        try (BufferedReader br = new BufferedReader(new FileReader(directorio))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return;
        }

        if (lineas.isEmpty()) {
            System.out.println("No hay registros en el fichero.");
            return;
        }

        // aqui le enseño los alumnos que hay (esto en verdad no se si hay que hacerlo)
        System.out.println("Lista de alumnos (elige un número):");
        for (int i = 0; i < lineas.size(); i++) {
            System.out.println((i + 1) + ") " + lineas.get(i));
        }

        System.out.print("Introduce el número del alumno a mostrar: ");
        String entrada = sc.nextLine().trim();

        try {
            int opcion = Integer.parseInt(entrada);

            if (opcion >= 1 && opcion <= lineas.size()) {
                String registro = lineas.get(opcion - 1);
                // y aqui si esta el alumno le muestro sus datos
                String[] partes = registro.split(" - ");
                if (partes.length >= 3) {
                    System.out.println("Alumno encontrado:");
                    System.out.println("ID: " + partes[0].trim());
                    System.out.println("Nombre: " + partes[1].trim());
                    System.out.println("Nota: " + partes[2].trim());
                } else {
                    // si esta mal escrito muestro to la linea, por si en algun momento la lio que no deberia
                    System.out.println("Registro: " + registro);
                }
            } else {
                System.out.println("Error: No existe usuario con ese número.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: debes introducir un número válido.");
        }

}   private static void modificarAlumno() {
        Scanner sc = new Scanner(System.in);
        String directorio = "C:\\Users\\Ismael\\Desktop\\demo\\Ejercicio2.txt";
        ArrayList<String> lineas = new ArrayList<>();

        // Aqui es una copia literal del metodo de ante
        try (BufferedReader br = new BufferedReader(new FileReader(directorio))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return;
        }

        if (lineas.isEmpty()) {
            System.out.println("No hay registros en el fichero.");
            return;
        }

        // y esto igual
        System.out.println("Lista de alumnos (elige el número del alumno que quieres modificar):");
        for (int i = 0; i < lineas.size(); i++) {
            System.out.println((i + 1) + ") " + lineas.get(i));
        }

        System.out.print("Introduce el número del alumno a modificar: ");
        String entrada = sc.nextLine().trim();

        int opcion;
        try {
            opcion = Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            System.out.println("Error: debes introducir un número válido.");
            return;
        }

        if (opcion < 1 || opcion > lineas.size()) {
            System.out.println("Error: No existe usuario con ese número.");
            return;
        }

        // aqui te muestro el registro que quieres modificar
        String registroActual = lineas.get(opcion - 1);
        System.out.println("Registro seleccionado: " + registroActual);

        // aqui muestro los datos y lo voy a utilizar mas adelante para cambiar los datos
        String[] partes = registroActual.split(" - ");
        if (partes.length >= 3) {
            System.out.println("ID actual: " + partes[0].trim());
            System.out.println("Nombre actual: " + partes[1].trim());
            System.out.println("Nota actual: " + partes[2].trim());
        }

        // y por aqui pido los datos por teclado
        int nuevoId = 0;
        String nuevoNombre = "";
        double nuevaNota = 0.0;

        try {
            System.out.print("Introduce el nuevo ID del alumno (ENTER para mantener el actual): ");
            String sId = sc.nextLine().trim();
            if (sId.isEmpty() && partes.length >= 1) {
                try {
                    nuevoId = Integer.parseInt(partes[0].trim());
                } catch (NumberFormatException ex) {
                    nuevoId = 0; // valor por defecto si no se puede parsear
                }
            } else {
                nuevoId = Integer.parseInt(sId);
            }

            System.out.print("Introduce el nuevo nombre del alumno (ENTER para mantener el actual): ");
            String sNombre = sc.nextLine();
            if (sNombre.trim().isEmpty() && partes.length >= 2) {
                nuevoNombre = partes[1].trim();
            } else {
                nuevoNombre = sNombre.trim();
            }

            System.out.print("Introduce la nueva nota del alumno (ENTER para mantener la actual): ");
            String sNota = sc.nextLine().trim();
            if (sNota.isEmpty() && partes.length >= 3) {
                try {
                    nuevaNota = Double.parseDouble(partes[2].trim());
                } catch (NumberFormatException ex) {
                    nuevaNota = 0.0;
                }
            } else {
                nuevaNota = Double.parseDouble(sNota);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: formato de número incorrecto. Operación cancelada.");
            return;
        }

        // reemplazo la linea del arraid list
        String nuevaLinea = nuevoId + " - " + nuevoNombre + " - " + nuevaNota;
        lineas.set(opcion - 1, nuevaLinea);

        // Y aqui reescribo los datos
        try (FileWriter writer = new FileWriter(directorio, false)) {
            for (String lin : lineas) {
                writer.write(lin + System.lineSeparator());
            }
            System.out.println("Alumno modificado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }
}

