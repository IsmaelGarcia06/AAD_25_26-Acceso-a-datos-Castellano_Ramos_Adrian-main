package com.example.demo;

import com.example.demo.model.Modules;
import com.example.demo.model.Student;
import com.example.demo.repository.ModuleRepository;
import com.example.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService implements CustomService<Student> {

    private final StudentRepository StudentRepository;
    private final ModuleRepository moduleRepository;

//    public StudentService(Student student) {
//        this.student = student;
//    }

    /**
     * @param entity
     * @return
     */
    @Override
    public boolean validate(Student entity) {
        return (entity.getDni() >0  && !entity.getName().isBlank());
    }

    public Student createStudent(final Student student){
        if (validate(student)) {

            return StudentRepository.create(student);
        }
        return null;
    }
}
