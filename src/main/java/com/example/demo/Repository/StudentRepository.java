package com.example.demo.Repository;

import com.example.demo.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class StudentRepository implements CrudRepository<Student> {
    /**
     * @param entity
     * @return
     */
    @Override
    public Student Create(Student entity) {
        log.info("Insert : {1}", entity.toString());
        return entity;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public Student Read(Student entity) {
        log.info("Read : {1}", entity.toString());
        return entity;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public Student Update(Student entity) {
        log.info("Update : {1}", entity.toString());
        return entity;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public boolean Delete(Student entity) {
        log.info("Delete : {1}", entity.toString());
        return true;
    }
}
