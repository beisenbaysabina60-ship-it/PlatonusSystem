package kz.platonus.repository;

import kz.platonus.database.DatabaseConnection;
import kz.platonus.database.DatabaseInitializer;
import kz.platonus.model.Student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StudentRepositoryTest {

    private StudentRepository repository;
    private Student student;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseInitializer.initialize();
        cleanTestData();

        repository = new StudentRepository();

        student = new Student(
                10001,
                "Repository Test Student",
                "repository10001@example.com",
                "ST10001",
                "GROUP10001"
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        cleanTestData();
    }

    private void cleanTestData() throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {

            String[] queries = {
                    "DELETE FROM attendance WHERE student_id = 10001",
                    "DELETE FROM grades WHERE student_id = 10001",
                    "DELETE FROM students WHERE id = 10001",
                    "DELETE FROM users WHERE id = 10001"
            };

            for (String query : queries) {
                try (PreparedStatement statement =
                             connection.prepareStatement(query)) {
                    statement.executeUpdate();
                }
            }
        }
    }

    @Test
    void testSaveStudent() {
        repository.save(student);

        Optional<Student> found = repository.findById(10001);

        assertTrue(found.isPresent());
        assertEquals("Repository Test Student", found.get().getFullName());
    }

    @Test
    void testFindById() {
        repository.save(student);

        Optional<Student> found = repository.findById(10001);

        assertTrue(found.isPresent());
        assertEquals(10001, found.get().getId());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Student> found = repository.findById(99999);

        assertTrue(found.isEmpty());
    }

    @Test
    void testFindAll() {
        repository.save(student);

        List<Student> students = repository.findAll();

        assertFalse(students.isEmpty());
    }

    @Test
    void testUpdateStudent() {
        repository.save(student);

        Student updatedStudent = new Student(
                10001,
                "Updated Repository Student",
                "repository10001@example.com",
                "ST10001",
                "GROUP-UPDATED"
        );

        repository.update(updatedStudent);

        Optional<Student> found = repository.findById(10001);

        assertTrue(found.isPresent());
        assertEquals(
                "Updated Repository Student",
                found.get().getFullName()
        );
        assertEquals("GROUP-UPDATED", found.get().getGroupName());
    }

    @Test
    void testDeleteStudent() {
        repository.save(student);

        repository.deleteById(10001);

        Optional<Student> found = repository.findById(10001);

        assertTrue(found.isEmpty());
    }

    @Test
    void testCount() {
        repository.save(student);

        long count = repository.count();

        assertTrue(count >= 1);
    }
}