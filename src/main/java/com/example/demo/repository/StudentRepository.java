package com.example.demo.repository;

import com.example.demo.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
@Slf4j
public class StudentRepository implements CrudRepository<Student> {
    // SQL statements
    private static final String SQL_INSERT = """
 INSERT INTO alumno (id_alumno, nombre, email)
 VALUES (?, ?, ?)
 """;
    private static final String SQL_SELECT_BY_ID = """
 SELECT id_alumno, nombre, email
 FROM alumno
 WHERE id = ?
 """;
    private static final String SQL_UPDATE = """
 UPDATE alumno
 SET nombre = ?, email = ?
 WHERE id = ?
 """;
    private static final String SQL_DELETE = """
 DELETE FROM alumno
 WHERE id = ?
 """;
    private final DataSource dataSource;
    public StudentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public Student create(Student entity) {
        if (entity == null) throw new IllegalArgumentException("Student cannot be null");
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entity.getDni());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getEmail());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setDni(keys.getInt(1));
                }
            }
            log.info("create OK: {}", entity);
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating Student", e);
        }
    }
    @Override
    public Student read(Student entity) {
        if (entity == null || entity.getDni() >0) {
            throw new IllegalArgumentException("read requires a Student with non-null id");
        }
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, entity.getDni());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Student found = mapRow(rs);
                    log.info("read OK: {}", found);
                    return found;
                } else {
                    log.info("read: no student found with dni={}", entity.getDni());
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading Student dni=" + entity.getDni(), e);
        }
    }
    @Override
    public Student update(Student entity) {
        if (entity == null || entity.getDni() >0) {
            throw new IllegalArgumentException("update requires a Student with non-null id");
        }
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, String.valueOf(entity.getDni()));
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getEmail());
            ps.setInt(5, entity.getDni());
            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new RuntimeException("Student not found for update: dni=" + entity.getDni());
            }
            log.info("update OK: {}", entity);
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Student dni=" + entity.getDni(), e);
        }
    }
    @Override
    public boolean delete(Student entity) {
        if (entity == null || entity.getDni() >0) {
            throw new IllegalArgumentException("delete requires a Student with non-null dni");
        }
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, entity.getDni());
            int deleted = ps.executeUpdate();
            boolean ok = deleted > 0;
            log.info("delete {} for dni={}", ok ? "OK" : "NOOP", entity.getDni());
            return ok;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Student dni=" + entity.getDni(), e);
        }
    }
    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setDni(rs.getInt("dni"));
        s.setName(rs.getString("name"));
        s.setEmail(rs.getString("email"));
        return s;
    }
}