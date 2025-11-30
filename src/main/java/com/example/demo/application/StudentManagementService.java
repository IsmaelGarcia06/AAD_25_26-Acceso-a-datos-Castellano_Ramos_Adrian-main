package com.example.demo.application;

import com.example.demo.model.Enrollment;
import com.example.demo.model.Module;
import com.example.demo.model.Student;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.ModuleRepository;
import com.example.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentManagementService {

    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PostgresqlDriver postgresqlDriver;

    public Module createModule(Module module) {
        if (module == null || module.getCode() == null || module.getCode().isBlank()) {
            throw new IllegalArgumentException("Module code cannot be null or empty");
        }

        Module existing = moduleRepository.findByCode(module.getCode());
        if (existing != null) {
            log.info("Module already exists with code: {}", module.getCode());
            return existing;
        }

        Module created = moduleRepository.insert(module);
        log.info("Module created: {}", created.getName());
        return created;
    }

    public Student createStudent(Student student) {
        if (student == null || student.getNif() == null || student.getNif().isBlank()) {
            throw new IllegalArgumentException("Student NIF cannot be null or empty");
        }

        if (student.getName() == null || student.getName().isBlank()) {
            throw new IllegalArgumentException("Student name cannot be null or empty");
        }

        if (student.getEmail() == null || student.getEmail().isBlank()) {
            throw new IllegalArgumentException("Student email cannot be null or empty");
        }

        Student existing = studentRepository.findByNif(student.getNif());
        if (existing != null) {
            log.info("Student already exists with NIF: {}", student.getNif());
            return existing;
        }

        Student created = studentRepository.insert(student);
        log.info("Student created: {}", created.getName());
        return created;
    }

    public Enrollment enrollStudentInModule(Integer studentId, Integer moduleId) {
        try {
            postgresqlDriver.beginTransaction();

            Student student = studentRepository.findById(studentId);
            if (student == null) {
                throw new IllegalArgumentException("Student not found: " + studentId);
            }

            Module module = moduleRepository.findById(moduleId);
            if (module == null) {
                throw new IllegalArgumentException("Module not found: " + moduleId);
            }

            Enrollment created = enrollmentRepository.create(
                    new Enrollment(null, student.getId(), module.getId(), LocalDate.now())
            );

            postgresqlDriver.commit();

            log.info("Student {} enrolled in module {}", student.getName(), module.getName());
            return created;

        } catch (Exception e) {
            postgresqlDriver.rollback();
            log.error("Error enrolling student in module", e);
            throw new RuntimeException("Error enrolling student in module", e);
        }
    }
}