package com.example.demo.repository;

import com.example.demo.model.Module;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class ModuleRepository {

    // SQL adaptado al esquema del PDF: modulo(id_modulo, codigo, nombre, horas)
    private static final String SQL_INSERT = """
            INSERT INTO modulo (codigo, nombre, horas)
            VALUES (?, ?, ?)
            RETURNING id_modulo
            """;

    private static final String SQL_SELECT_ALL = """
            SELECT id_modulo, codigo, nombre, horas
            FROM modulo
            ORDER BY id_modulo
            """;

    private static final String SQL_SELECT_BY_ID = """
            SELECT id_modulo, codigo, nombre, horas
            FROM modulo
            WHERE id_modulo = ?
            """;

    private static final String SQL_SELECT_BY_CODE = """
            SELECT id_modulo, codigo, nombre, horas
            FROM modulo
            WHERE codigo = ?
            """;

    private static final String SQL_UPDATE = """
            UPDATE modulo
            SET codigo = ?, nombre = ?, horas = ?
            WHERE id_modulo = ?
            """;

    private static final String SQL_DELETE = """
            DELETE FROM modulo
            WHERE id_modulo = ?
            """;

    private final DataSource dataSource;

    public ModuleRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Module insert(Module module) {
        if (module == null) {
            throw new IllegalArgumentException("Module cannot be null");
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            ps.setString(1, module.getCode());
            ps.setString(2, module.getName());
            ps.setInt(3, module.getHours());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    module.setId(rs.getInt("id_modulo"));
                    log.info("Module inserted with ID: {}", module.getId());
                    return module;
                }
            }

        } catch (SQLException e) {
            log.error("Error inserting module: {}", module.getCode(), e);
            throw new RuntimeException("Error inserting module: " + e.getMessage(), e);
        }

        return null;
    }

    public List<Module> findAll() {
        List<Module> modules = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Module module = mapRow(rs);
                modules.add(module);
            }

            log.info("Found {} modules", modules.size());
            return modules;

        } catch (SQLException e) {
            log.error("Error finding all modules", e);
            throw new RuntimeException("Error finding all modules: " + e.getMessage(), e);
        }
    }

    public Module findById(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Module module = mapRow(rs);
                    log.info("Module found: {}", module.getName());
                    return module;
                } else {
                    log.warn("No module found with id={}", id);
                    return null;
                }
            }

        } catch (SQLException e) {
            log.error("Error finding module by ID: {}", id, e);
            throw new RuntimeException("Error finding module by ID: " + e.getMessage(), e);
        }
    }

    public Module findByCode(String code) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_CODE)) {

            ps.setString(1, code);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Module module = mapRow(rs);
                    log.info("Module found by code: {}", module.getName());
                    return module;
                } else {
                    log.info("No module found with code={}", code);
                    return null;
                }
            }

        } catch (SQLException e) {
            log.error("Error finding module by code: {}", code, e);
            throw new RuntimeException("Error finding module by code: " + e.getMessage(), e);
        }
    }

    public Module update(Module module) {
        if (module == null || module.getId() == null) {
            throw new IllegalArgumentException("Update requires a Module with valid ID");
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, module.getCode());
            ps.setString(2, module.getName());
            ps.setInt(3, module.getHours());
            ps.setInt(4, module.getId());

            int updated = ps.executeUpdate();

            if (updated == 0) {
                log.warn("No module updated with id={}", module.getId());
                throw new RuntimeException("Module not found for update: id=" + module.getId());
            }

            log.info("Module updated: {}", module.getName());
            return module;

        } catch (SQLException e) {
            log.error("Error updating module with ID: {}", module.getId(), e);
            throw new RuntimeException("Error updating module: " + e.getMessage(), e);
        }
    }

    public boolean delete(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, id);
            int deleted = ps.executeUpdate();
            boolean ok = deleted > 0;

            log.info("Delete {} for module id={}", ok ? "OK" : "NOOP", id);
            return ok;

        } catch (SQLException e) {
            log.error("Error deleting module with ID: {}", id, e);
            throw new RuntimeException("Error deleting module: " + e.getMessage(), e);
        }
    }

    private Module mapRow(ResultSet rs) throws SQLException {
        Module module = new Module();
        module.setId(rs.getInt("id_modulo"));
        module.setCode(rs.getString("codigo"));
        module.setName(rs.getString("nombre"));
        module.setHours(rs.getInt("horas"));
        return module;
    }
}