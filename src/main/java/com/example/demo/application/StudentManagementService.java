package com.example.demo.application;

import com.example.demo.model.Enrollment;
import com.example.demo.model.Module;
import com.example.demo.model.Student;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.ModuleRepository;
import com.example.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StudentManagementService {

    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public Student createStudent(Student s) {
        return studentRepository.findByNif(s.getNif())
                .orElseGet(() -> studentRepository.save(s));
    }

    @Transactional
    public Module createModule(Module m) {
        return moduleRepository.findByCode(m.getCode())
                .orElseGet(() -> moduleRepository.save(m));
    }

    @Transactional
    public Enrollment enrollStudentInModule(Integer studentId, Integer moduleId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setModule(module);
        enrollment.setEnrollmentDate(LocalDate.now());

        return enrollmentRepository.save(enrollment);
    }

    @Transactional(readOnly = true)
    public int countEnrollments(Integer studentId) {
        return enrollmentRepository.countByStudentId(studentId);
    }
}