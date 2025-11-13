package com.example.demo;

import com.example.demo.model.Student;
import com.example.demo.repository.ModuleRepository;
import com.example.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService implements CustomService<Student> {

    private final StudentRepository StudentRepository;
    private final ModuleRepository moduleRepository;

    /**
     * @param entity
     * @return
     */
    @Override
    public boolean validate(Student entity) {
        return (entity.getDni() > 0 && !entity.getName().isBlank());
    }

    public Student createStudent(final Student student) {
        if (validate(student)) {

            return StudentRepository.create(student);
        }
        return null;
    }

    // Añadir este método al StudentService
    public void createStudentsTransaction(List<Student> students) {
        try {
            StudentRepository.transactionalOperation(students);
        } catch (Exception e) {
            log.error("Error in transactional operation: {}", e.getMessage());
            throw new RuntimeException("Failed to create students transactionally: " + e.getMessage(), e);
        }
    }
}
