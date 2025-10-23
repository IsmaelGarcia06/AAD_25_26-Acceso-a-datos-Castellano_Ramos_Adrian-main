package com.example.demo.Repository;

import com.example.demo.model.Module;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ModuleRepository implements CrudRepository<Module> {
    /**
     * @param entity
     * @return
     */
    @Override
    public Module Create(Module entity) {
        log.info("Insert : {1}", entity.toString());
        return entity;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public Module Read(Module entity) {
        log.info("Read : {1}", entity.toString());
        return entity;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public Module Update(Module entity) {
        log.info("Update : {1}", entity.toString());
        return entity;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public boolean Delete(Module entity) {
        log.info("Delete : {1}", entity.toString());
        return true;
    }


}
