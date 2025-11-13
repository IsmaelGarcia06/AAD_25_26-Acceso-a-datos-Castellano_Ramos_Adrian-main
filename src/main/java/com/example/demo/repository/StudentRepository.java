package com.example.demo.repository;

import com.example.demo.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Repository
@Slf4j
public class StudentRepository implements CrudRepository<Student> {
    private static final String SQL_INSERT = """
            INSERT INTO alumno (id_alumno, nombre, email)
            VALUES (?, ?, ?)
            """;
    private static final String SQL_SELECT_BY_ID = """
            SELECT id_alumno, nombre, email
            FROM alumno
            WHERE id_alumno = ?
            """; // Corregido: id -> id_alumno
    private static final String SQL_UPDATE = """
            UPDATE alumno
            SET nombre = ?, email = ?
            WHERE id_alumno = ?
            """; // Corregido: id -> id_alumno
    private static final String SQL_DELETE = """
            DELETE FROM alumno
            WHERE id_alumno = ?
            """; // Corregido: id -> id_alumno

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

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating student failed, no rows affected.");
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setDni(keys.getInt(1));
                }
            }

            log.info("create OK: {}", entity);
            return entity;

        } catch (SQLException e) {
            log.error("Error creating student with DNI: {}", entity.getDni(), e);
            throw new RuntimeException("Error creating Student: " + e.getMessage(), e);
        }
    }

    @Override
    public Student read(Student entity) {
        if (entity == null || entity.getDni() <= 0) { // Corregida condición
            throw new IllegalArgumentException("read requires a Student with valid dni");
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
                    log.warn("No student found with dni={}", entity.getDni());
                    return null;
                }
            }

        } catch (SQLException e) {
            log.error("Error reading student with DNI: {}", entity.getDni(), e);
            throw new RuntimeException("Error reading Student: " + e.getMessage(), e);
        }
    }

    @Override
    public Student update(Student entity) {
        if (entity == null || entity.getDni() <= 0) { // Corregida condición
            throw new IllegalArgumentException("update requires a Student with valid dni");
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, entity.getName());
            ps.setString(2, entity.getEmail());
            ps.setInt(3, entity.getDni()); // Corregidos los índices

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new RuntimeException("Student not found for update: dni=" + entity.getDni());
            }

            log.info("update OK: {}", entity);
            return entity;

        } catch (SQLException e) {
            log.error("Error updating student with DNI: {}", entity.getDni(), e);
            throw new RuntimeException("Error updating Student: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Student entity) {
        if (entity == null || entity.getDni() <= 0) { // Corregida condición
            throw new IllegalArgumentException("delete requires a Student with valid dni");
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, entity.getDni());
            int deleted = ps.executeUpdate();
            boolean ok = deleted > 0;

            log.info("delete {} for dni={}", ok ? "OK" : "NOOP", entity.getDni());
            return ok;

        } catch (SQLException e) {
            log.error("Error deleting student with DNI: {}", entity.getDni(), e);
            throw new RuntimeException("Error deleting Student: " + e.getMessage(), e);
        }
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setDni(rs.getInt("id_alumno")); // Corregido: dni -> id_alumno
        s.setName(rs.getString("nombre")); // Corregido: name -> nombre
        s.setEmail(rs.getString("email"));
        return s;
    }

    // Método para operación transaccional
    public void transactionalOperation(List<Student> students) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            for (Student student : students) {
                try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
                    ps.setInt(1, student.getDni());
                    ps.setString(2, student.getName());
                    ps.setString(3, student.getEmail());
                    ps.executeUpdate();
                }

                // Simular error en el segundo elemento
                if (student.getDni() == students.get(1).getDni()) {
                    throw new SQLException("Simulated error in transaction");
                }
            }

            conn.commit();
            log.info("Transaction completed successfully");

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    log.info("Transaction rolled back due to error: {}", e.getMessage());
                } catch (SQLException rollbackEx) {
                    log.error("Error during rollback: {}", rollbackEx.getMessage());
                }
            }
            throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    log.error("Error closing connection: {}", e.getMessage());
                }
            }
        }
    }

    // SOLO PARA PRUEBAS - ELIMINAR DESPUÉS
    public void testDangerousDelete() {
        String SQL_DELETE_NO_WHERE = "DELETE FROM alumno";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE_NO_WHERE)) {

            int deleted = ps.executeUpdate();
            log.warn("DELETE sin WHERE ejecutado - {} registros eliminados", deleted);

        } catch (SQLException e) {
            log.error("Error en DELETE sin WHERE: {}", e.getMessage());
            throw new RuntimeException("Error en operación peligrosa: " + e.getMessage(), e);
        }
    }
}