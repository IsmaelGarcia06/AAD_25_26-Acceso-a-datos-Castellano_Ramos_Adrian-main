package com.example.demo;

import com.example.demo.Repository.StudentService;
import com.example.demo.model.Module;
import com.example.demo.model.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class DemoApplication implements CommandLineRunner {

    private final StudentService studentService;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Student student = new Student("12345678A", "Doe", "John", "Math");
        Module module1 = new Module("MATH101", "Mathematics");
        Module module2 = new Module("PHY101", "Physics");
        List<Module> modules = List.of(module1, module2);
        studentService.createStudent(student, modules);
        Student create = studentService.createStudent(student, modules);
        if (create != null) {
            log.info("Student created successfully: {}", create);
        } else {
            log.error("Failed to create student");
        }
    }
}