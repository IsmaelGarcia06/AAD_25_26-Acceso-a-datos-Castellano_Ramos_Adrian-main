package com.example.demo.repository;

import com.example.demo.model.Enrollment;
import com.example.demo.model.Module;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class EnrollmentRepository {

    private static final String SQL_INSERT = """
            INSERT INTO matricula (id_alumno, id_modulo, fecha)
            VALUES (?, ?, ?)
            """;

    private static final String SQL_SELECT_ALL = """
            SELECT id_alumno, id_modulo, fecha
            FROM matricula
            ORDER BY fecha DESC
            """;

    private static final String SQL_SELECT_BY_STUDENT = """
            SELECT id_alumno, id_modulo, fecha
            FROM matricula
            WHERE id_alumno = ?
            ORDER BY fecha DESC
            """;

    private static final String SQL_DELETE = """
            DELETE FROM matricula
            WHERE id_alumno = ? AND id_modulo = ?
            """;

    private static final String SQL_CALL_COUNT_ENROLLMENTS = "{ ? = call count_enrollments(?) }";

    private final DataSource dataSource;

    public EnrollmentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public Enrollment create(Enrollment enrollment) {
        if (enrollment == null) {
            throw new IllegalArgumentException("Enrollment cannot be null");
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            ps.setInt(1, enrollment.getStudentId());
            ps.setInt(2, enrollment.getModuleId());
            ps.setDate(3, Date.valueOf(enrollment.getDate()));

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                log.info("Enrollment created: studentId={}, moduleId={}",
                        enrollment.getStudentId(), enrollment.getModuleId());
                return enrollment;
            }

        } catch (SQLException e) {
            log.error("Error creating enrollment: studentId={}, moduleId={}",
                    enrollment.getStudentId(), enrollment.getModuleId(), e);
            throw new RuntimeException("Error creating enrollment: " + e.getMessage(), e);
        }

        return null;
    }


    public Enrollment createEnrollment(Enrollment enrollment, List<Module> modules) {
        return create(enrollment);
    }


    public List<Enrollment> findAll() {
        List<Enrollment> enrollments = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Enrollment enrollment = mapRow(rs);
                enrollments.add(enrollment);
            }

            log.info("Found {} enrollments", enrollments.size());
            return enrollments;

        } catch (SQLException e) {
            log.error("Error finding all enrollments", e);
            throw new RuntimeException("Error finding all enrollments: " + e.getMessage(), e);
        }
    }

    public List<Enrollment> findByStudent(int studentId) {
        List<Enrollment> enrollments = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_STUDENT)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Enrollment enrollment = mapRow(rs);
                    enrollments.add(enrollment);
                }
            }

            log.info("Found {} enrollments for student {}", enrollments.size(), studentId);
            return enrollments;

        } catch (SQLException e) {
            log.error("Error finding enrollments for student: {}", studentId, e);
            throw new RuntimeException("Error finding enrollments by student: " + e.getMessage(), e);
        }
    }

    public boolean delete(int studentId, int moduleId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, studentId);
            ps.setInt(2, moduleId);

            int deleted = ps.executeUpdate();
            boolean ok = deleted > 0;

            log.info("Delete {} for enrollment: studentId={}, moduleId={}",
                    ok ? "OK" : "NOOP", studentId, moduleId);
            return ok;

        } catch (SQLException e) {
            log.error("Error deleting enrollment: studentId={}, moduleId={}",
                    studentId, moduleId, e);
            throw new RuntimeException("Error deleting enrollment: " + e.getMessage(), e);
        }
    }

    public int countEnrollments(int studentId) {
        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(SQL_CALL_COUNT_ENROLLMENTS)) {

            cs.registerOutParameter(1, Types.INTEGER);

            cs.setInt(2, studentId);

            cs.execute();

            int total = cs.getInt(1);

            log.info("Total enrollments for student {}: {}", studentId, total);
            return total;

        } catch (SQLException e) {
            log.error("Error counting enrollments for student: {}", studentId, e);
            throw new RuntimeException("Error counting enrollments: " + e.getMessage(), e);
        }
    }

    private Enrollment mapRow(ResultSet rs) throws SQLException {
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(rs.getInt("id_alumno"));
        enrollment.setModuleId(rs.getInt("id_modulo"));
        enrollment.setDate(rs.getDate("fecha").toLocalDate());
        // El campo 'id' no existe en la tabla matricula (clave compuesta)
        return enrollment;
    }
}