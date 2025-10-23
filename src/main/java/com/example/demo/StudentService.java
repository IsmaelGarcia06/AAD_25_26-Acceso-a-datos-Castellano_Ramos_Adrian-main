package com.example.demo;

import com.example.demo.model.Student;
@
public class StudentService implements Servicee<Student> {


    /**
     * @param entity
     * @return
     */
    @Override
    public boolean validate(Student entity) {
       return !entity.getDni().isBlank() && !entity.getName().isBlank();
    }
}
