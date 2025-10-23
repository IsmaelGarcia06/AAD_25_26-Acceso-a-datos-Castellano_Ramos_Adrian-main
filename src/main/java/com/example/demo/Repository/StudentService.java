package com.example.demo.Repository;

import com.example.demo.model.Module;
import com.example.demo.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StudentService implements Servicee<Student> {

    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;

    /**
     * @param entity
     * @return
     */
    @Override
    public boolean validate(Student entity) {
        return !entity.getDni().isBlank() && !entity.getName().isBlank();
    }

    public Student createStudent(final Student student, List<Module> modules) {
        if (validate(student)) {
            for (Module module : modules) {
                moduleRepository.Create(module);
            }
            return studentRepository.Create(student);
        }
        return null;
    }
}
