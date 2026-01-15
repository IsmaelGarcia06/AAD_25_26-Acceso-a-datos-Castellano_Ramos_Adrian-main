package com.example.demo.repository;

import com.example.demo.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    @Query("SELECT e FROM Enrollment e WHERE e.finalGrade >= :minGrade")
    List<Enrollment> findByMinFinalGrade(@Param("minGrade") Double minGrade);

    @Procedure(procedureName = "avg_grade_by_module")
    Double getAvgGradeByModule(@Param("moduleId") Integer moduleId);

    int countByStudentId(Integer studentId);
}