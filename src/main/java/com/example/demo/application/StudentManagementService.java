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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentManagementService {

    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;
    private final EnrollmentRepository enrollmentRepository;


    public Module createModule(Module module) {
        if (module == null || module.getCode() == null || module.getCode().isBlank()) {
            throw new IllegalArgumentException("Module code cannot be null or empty");
        }

        Module existing = moduleRepository.findByCode(module.getCode());
        if (existing != null) {
            log.info("Module already exists: {}", module.getCode());
            return existing;
        }

        Module created = moduleRepository.insert(module);
        log.info("Module created: {}", created.getName());
        return created;
    }

    public Student createStudent(Student s) {

        if (s == null)
            throw new IllegalArgumentException("Student cannot be null");

        if (s.getNif() == null || s.getNif().isBlank())
            throw new IllegalArgumentException("NIF cannot be empty");

        if (s.getName() == null || s.getName().isBlank())
            throw new IllegalArgumentException("Name cannot be empty");

        if (s.getEmail() == null || s.getEmail().isBlank())
            throw new IllegalArgumentException("Email cannot be empty");

        Student existing = studentRepository.findByNif(s.getNif());
        if (existing != null) {
            log.info("Student already exists: {}", s.getNif());
            return existing;
        }

        Student created = studentRepository.insert(s);
        log.info("Student created: {}", created.getName());
        return created;
    }

    @Transactional
    public Enrollment enrollStudentInModule(Integer studentId, Integer moduleId) {

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

        log.info("Student {} enrolled in module {}", student.getName(), module.getName());
        return created;
    }
}
