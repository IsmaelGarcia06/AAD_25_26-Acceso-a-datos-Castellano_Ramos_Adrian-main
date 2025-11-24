package com.example.demo.repository;

import com.example.demo.model.Student;
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
public class StudentRepository {

    // SQL adaptado al esquema del PDF: alumno(id_alumno, nif, nombre, email)
    private static final String SQL_INSERT = """
            INSERT INTO alumno (nif, nombre, email)
            VALUES (?, ?, ?)
            RETURNING id_alumno
            """;

    private static final String SQL_SELECT_ALL = """
            SELECT id_alumno, nif, nombre, email
            FROM alumno
            ORDER BY id_alumno
            """;

    private static final String SQL_SELECT_BY_ID = """
            SELECT id_alumno, nif, nombre, email
            FROM alumno
            WHERE id_alumno = ?
            """;

    private static final String SQL_SELECT_BY_NIF = """
            SELECT id_alumno, nif, nombre, email
            FROM alumno
            WHERE nif = ?
            """;

    private static final String SQL_UPDATE = """
            UPDATE alumno
            SET nif = ?, nombre = ?, email = ?
            WHERE id_alumno = ?
            """;

    private static final String SQL_DELETE = """
            DELETE FROM alumno
            WHERE id_alumno = ?
            """;

    private final DataSource dataSource;

    public StudentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Inserta un nuevo estudiante (según Paso 7)
     *
     * @param student Estudiante a insertar
     * @return Estudiante con ID generado
     */
    public Student insert(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            ps.setString(1, student.getNif());
            ps.setString(2, student.getName());
            ps.setString(3, student.getEmail());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    student.setId(rs.getInt("id_alumno"));
                    log.info("Student inserted with ID: {}", student.getId());
                    return student;
                }
            }

        } catch (SQLException e) {
            log.error("Error inserting student: {}", student.getNif(), e);
            throw new RuntimeException("Error inserting student: " + e.getMessage(), e);
        }

        return null;
    }

    /**
     * Obtiene todos los estudiantes (según Paso 7)
     *
     * @return Lista de todos los estudiantes
     */
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Student student = mapRow(rs);
                students.add(student);
            }

            log.info("Found {} students", students.size());
            return students;

        } catch (SQLException e) {
            log.error("Error finding all students", e);
            throw new RuntimeException("Error finding all students: " + e.getMessage(), e);
        }
    }

    /**
     * Busca un estudiante por ID (según Paso 7)
     *
     * @param id ID del estudiante
     * @return Estudiante encontrado o null
     */
    public Student findById(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Student student = mapRow(rs);
                    log.info("Student found: {}", student.getName());
                    return student;
                } else {
                    log.warn("No student found with id={}", id);
                    return null;
                }
            }

        } catch (SQLException e) {
            log.error("Error finding student by ID: {}", id, e);
            throw new RuntimeException("Error finding student by ID: " + e.getMessage(), e);
        }
    }

    /**
     * Busca un estudiante por NIF (útil para verificar existencia)
     *
     * @param nif NIF del estudiante
     * @return Estudiante encontrado o null
     */
    public Student findByNif(String nif) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_NIF)) {

            ps.setString(1, nif);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Student student = mapRow(rs);
                    log.info("Student found by NIF: {}", student.getName());
                    return student;
                } else {
                    log.info("No student found with NIF={}", nif);
                    return null;
                }
            }

        } catch (SQLException e) {
            log.error("Error finding student by NIF: {}", nif, e);
            throw new RuntimeException("Error finding student by NIF: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza un estudiante existente (según Paso 7)
     *
     * @param student Estudiante con datos actualizados
     * @return Estudiante actualizado
     */
    public Student update(Student student) {
        if (student == null || student.getId() == null) {
            throw new IllegalArgumentException("Update requires a Student with valid ID");
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, student.getNif());
            ps.setString(2, student.getName());
            ps.setString(3, student.getEmail());
            ps.setInt(4, student.getId());

            int updated = ps.executeUpdate();

            if (updated == 0) {
                log.warn("No student updated with id={}", student.getId());
                throw new RuntimeException("Student not found for update: id=" + student.getId());
            }

            log.info("Student updated: {}", student.getName());
            return student;

        } catch (SQLException e) {
            log.error("Error updating student with ID: {}", student.getId(), e);
            throw new RuntimeException("Error updating student: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un estudiante por ID (según Paso 7)
     *
     * @param id ID del estudiante a eliminar
     * @return true si se eliminó, false si no existía
     */
    public boolean delete(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, id);
            int deleted = ps.executeUpdate();
            boolean ok = deleted > 0;

            log.info("Delete {} for student id={}", ok ? "OK" : "NOOP", id);
            return ok;

        } catch (SQLException e) {
            log.error("Error deleting student with ID: {}", id, e);
            throw new RuntimeException("Error deleting student: " + e.getMessage(), e);
        }
    }

    /**
     * Mapea un ResultSet a un objeto Student
     *
     * @param rs ResultSet con los datos
     * @return Objeto Student
     * @throws SQLException si hay error al leer datos
     */
    private Student mapRow(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id_alumno"));
        student.setNif(rs.getString("nif"));
        student.setName(rs.getString("nombre"));
        student.setEmail(rs.getString("email"));
        // El campo 'curse' y 'modules' se cargarían en el servicio si es necesario
        return student;
    }
}