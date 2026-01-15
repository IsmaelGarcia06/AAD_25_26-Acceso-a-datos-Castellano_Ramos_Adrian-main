package com.example.demo;

import com.example.demo.application.StudentManagementService;
import com.example.demo.model.Enrollment;
import com.example.demo.model.Module;
import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class Application implements CommandLineRunner {

    private final StudentManagementService managementService;
    private final StudentRepository studentRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        try {
            Student miriam = new Student();
            miriam.setNif("66280457T");
            miriam.setName("Miriam");
            miriam.setEmail("miriam@g.educaand.es");
            miriam.setCurse("DAW");

            com.example.demo.model.Module programacion = new Module();
            programacion.setCode("0485");
            programacion.setName("Programación");
            programacion.setHours(250);

            miriam = managementService.createStudent(miriam);
            log.info("Alumno creado: {}", miriam);

            programacion = managementService.createModule(programacion);
            log.info("Módulo creado: {}", programacion);

            Enrollment enrollment = managementService.enrollStudentInModule(miriam.getId(), programacion.getId());
            log.info("Matricula realizada: {}", enrollment);

            int count = managementService.countEnrollments(miriam.getId());
            log.info("{} módulos matriculados para el alumno {}", count, miriam.getName());

            // Forzar el error del Paso 7
            throw new RuntimeException("Forzando rollback de la transacción");

        } catch (RuntimeException e) {
            log.error("ERROR: {}", e.getMessage());
        }
    }
}