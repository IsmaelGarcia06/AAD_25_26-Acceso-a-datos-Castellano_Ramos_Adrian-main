package com.example.demo.repository;

import com.example.demo.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Slf4j
@TestPropertySource(locations = "classpath:application-test.properties")
public class StudentRepositoryErrorTest {

    @Autowired
    private StudentRepository studentRepository;

    // Test 1: INSERT duplicado
    @Test
    void testDuplicateInsert() {
        log.info("=== TEST 1: INSERT duplicado ===");

        Student student1 = new Student(1, "Juan", "juan@email.com");

        try {
            studentRepository.create(student1);
            log.info("Primer INSERT exitoso");
        } catch (Exception e) {
            log.info("Primer INSERT falló: {}", e.getMessage());
        }

        // Intentar insertar mismo ID again
        try {
            studentRepository.create(student1);
            log.info("Segundo INSERT exitoso (inesperado)");
        } catch (Exception e) {
            log.error("Duplicate insert (esperado): {}", e.getMessage());
        }
    }

    // Test 2: UPDATE con columna inexistente
    @Test
    void testUpdateWithNonExistentColumn() {
        log.info("=== TEST 2: UPDATE con columna inexistente ===");

        // Esto se probaría modificando temporalmente el SQL en StudentRepository
        // No lo ejecutamos automáticamente para no romper el código
        String SQL_UPDATE_WRONG = "UPDATE alumno SET non_existent_column = ? WHERE id_alumno = ?";
        log.info("SQL mal formado para prueba: {}", SQL_UPDATE_WRONG);
        log.info("Este test debe ejecutarse manualmente modificando temporalmente el código");
    }

    // Test 3: DELETE sin WHERE
    @Test
    void testDeleteWithoutWhere() {
        log.info("=== TEST 3: DELETE sin WHERE ===");

        // SOLO EJECUTAR EN BASE DE DATOS DE PRUEBAS
        String SQL_DELETE_NO_WHERE = "DELETE FROM alumno";
        log.info("SQL peligroso (solo para pruebas): {}", SQL_DELETE_NO_WHERE);
        log.info("ADVERTENCIA: No ejecutar en producción - borraría todos los registros");

        // Para probar realmente, necesitarías un método temporal en StudentRepository
        // testDangerousDelete();
    }

    // Test 4: Transacciones con rollback
    @Test
    void testTransactionalRollback() {
        log.info("=== TEST 4: Transacción con rollback ===");

        List<Student> students = Arrays.asList(
                new Student(100, "Estudiante1", "est1@email.com"),
                new Student(101, "Estudiante2", "est2@email.com"), // Este fallará
                new Student(102, "Estudiante3", "est3@email.com")
        );

        try {
            studentRepository.transactionalOperation(students);
            log.info("Transacción completada (inesperado)");
        } catch (Exception e) {
            log.error("Transacción falló (esperado): {}", e.getMessage());
            log.info("Verificar en BD que ningún estudiante fue insertado debido al rollback");
        }
    }
}