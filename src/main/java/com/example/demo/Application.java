package com.example.demo;

import com.example.demo.application.StudentManagementService;
import com.example.demo.model.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class Application implements CommandLineRunner {

    private final StudentManagementService studentService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Student vito = new Student(1, "John", "asd@gmail.com");
        Student create = studentService.createStudent(vito);
        if (create != null) {
            log.info("Create: {}", create);
        } else {
            log.error("Student not valid");
        }
    }
}
