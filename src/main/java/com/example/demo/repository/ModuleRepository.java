package com.example.demo.repository;

import com.example.demo.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModuleRepository extends JpaRepository<Module, Integer> {
    Optional<Module> findByCode(String code);
}