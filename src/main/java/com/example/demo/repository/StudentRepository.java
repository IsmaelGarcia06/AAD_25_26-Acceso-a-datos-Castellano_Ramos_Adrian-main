package com.example.demo.repository;

import com.example.demo.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class StudentRepository {

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

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Student> mapper = (rs, rowNum) -> {
        Student s = new Student();
        s.setId(rs.getInt("id_alumno"));
        s.setNif(rs.getString("nif"));
        s.setName(rs.getString("nombre"));
        s.setEmail(rs.getString("email"));
        return s;
    };

    public StudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Student insert(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }

        Integer id = jdbcTemplate.queryForObject(
                SQL_INSERT,
                Integer.class,
                student.getNif(),
                student.getName(),
                student.getEmail()
        );

        if (id == null) {
            throw new RuntimeException("Error inserting student, no ID returned");
        }

        student.setId(id);
        log.info("Student inserted with ID: {}", id);
        return student;
    }

    public List<Student> findAll() {
        List<Student> list = jdbcTemplate.query(SQL_SELECT_ALL, mapper);
        log.info("Found {} students", list.size());
        return list;
    }

    public Student findById(int id) {
        try {
            return jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, mapper, id);
        } catch (Exception e) {
            log.warn("No student found with id={}", id);
            return null;
        }
    }

    public Student findByNif(String nif) {
        try {
            return jdbcTemplate.queryForObject(SQL_SELECT_BY_NIF, mapper, nif);
        } catch (Exception e) {
            log.info("No student found with NIF={}", nif);
            return null;
        }
    }

    public Student update(Student student) {
        if (student == null || student.getId() == null) {
            throw new IllegalArgumentException("Update requires a Student with valid ID");
        }

        int updated = jdbcTemplate.update(
                SQL_UPDATE,
                student.getNif(),
                student.getName(),
                student.getEmail(),
                student.getId()
        );

        if (updated == 0) {
            throw new RuntimeException("Student not found for update: id=" + student.getId());
        }

        log.info("Student updated: {}", student.getName());
        return student;
    }

    public boolean delete(int id) {
        int deleted = jdbcTemplate.update(SQL_DELETE, id);
        boolean ok = deleted > 0;

        log.info("Delete {} for student id={}", ok ? "OK" : "NOOP", id);

        return ok;
    }
}
