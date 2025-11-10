package com.example.demo.repository;

import com.example.demo.model.Modules;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ModuleRepository implements CrudRepository<Modules> {


    /**
     * @param entity
     * @return
     */
    @Override
    public Modules create(Modules entity) {
        log.info("insert: {}", entity.toString());
        return null;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public Modules read(Modules entity) {
        log.info("read: {}", entity.toString());
        return null;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public Modules update(Modules entity) {
        log.info("update: {}", entity.toString());
        return null;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public boolean delete(Modules entity) {
        log.info("delete: {}", entity.toString());
        return false;
    }
}
