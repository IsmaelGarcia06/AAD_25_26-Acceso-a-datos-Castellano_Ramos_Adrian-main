package com.example.demo.repository;

import com.example.demo.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class PersonRepository implements CrudRepository<Person> {


    /**
     * @param entity
     * @return
     */
    @Override
    public Person create(Person entity) {
        log.info("insert: {}", entity.toString());
        return null;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public Person read(Person entity) {
        log.info("read: {}", entity.toString());
        return null;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public Person update(Person entity) {
        log.info("update: {}", entity.toString());
        return null;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public boolean delete(Person entity) {
        log.info("delete: {}", entity.toString());
        return false;
    }
}
