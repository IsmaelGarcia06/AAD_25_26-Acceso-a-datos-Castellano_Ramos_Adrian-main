package com.example.demo.Repository;

public interface CrudRepository<T> {
    T Create(T entity);

    T Read(T entity);

    T Update(T entity);

    boolean Delete(T entity);
}
