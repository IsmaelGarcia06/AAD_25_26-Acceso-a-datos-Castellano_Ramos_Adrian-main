package com.example.demo;

import com.example.demo.application.StudentManagementService;
import com.example.demo.model.Module;
import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class Application implements CommandLineRunner {

    private final StudentManagementService studentManagementService;
    private final StudentRepository studentRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Student miriam = new Student(null, "66280457T", "Miriam",
                "miriam@g.educaand.es", "DAW", List.of());

        Module programacion = new Module(null, "0485", "Programación", 250);

        miriam = studentManagementService.createStudent(miriam);
        programacion = studentManagementService.createModule(programacion);

        studentManagementService.enrollStudentInModule(miriam.getId(),
                programacion.getId());

        studentRepository.delete(miriam.getId());
    }
}