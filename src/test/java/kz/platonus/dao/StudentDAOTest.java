package kz.platonus.dao;

import kz.platonus.database.DatabaseConnection;
import kz.platonus.database.DatabaseInitializer;
import kz.platonus.model.Student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentDAOTest {

    private final StudentDAO studentDAO = new StudentDAO();

    @BeforeEach
    void setUp() {
        DatabaseInitializer.initialize();
        cleanTestData();
    }

    @AfterEach
    void tearDown() {
        cleanTestData();
    }

    private void cleanTestData() {
        int[] ids = {9001, 9002, 9003, 9004, 9005};

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            for (int id : ids) {
                statement.executeUpdate("DELETE FROM attendance WHERE student_id = " + id);
                statement.executeUpdate("DELETE FROM grades WHERE student_id = " + id);
                statement.executeUpdate("DELETE FROM students WHERE id = " + id);
                statement.executeUpdate("DELETE FROM users WHERE id = " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Test cleanup failed", e);
        }
    }

    private Student createStudent(int id) {
        return new Student(
                id,
                "Test Student " + id,
                "student" + id + "@test.com",
                "ST" + id,
                "IT-2301"
        );
    }

    @Test
    void shouldAddStudentToDatabase() throws SQLException {
        Student student = createStudent(9001);

        studentDAO.addStudent(student);

        Student savedStudent = studentDAO.findById(9001);

        assertNotNull(savedStudent);
        assertEquals(student.getFullName(), savedStudent.getFullName());
        assertEquals(student.getEmail(), savedStudent.getEmail());
        assertEquals(student.getStudentNumber(), savedStudent.getStudentNumber());
    }

    @Test
    void shouldFindStudentById() throws SQLException {
        Student student = createStudent(9002);

        studentDAO.addStudent(student);

        Student found = studentDAO.findById(9002);

        assertNotNull(found);
        assertEquals(9002, found.getId());
    }

    @Test
    void shouldReturnAllStudents() throws SQLException {
        Student student1 = createStudent(9003);
        Student student2 = createStudent(9004);

        studentDAO.addStudent(student1);
        studentDAO.addStudent(student2);

        List<Student> students = studentDAO.findAll();

        assertTrue(students.stream()
                .anyMatch(student -> student.getId() == 9003));

        assertTrue(students.stream()
                .anyMatch(student -> student.getId() == 9004));
    }

    @Test
    void shouldUpdateStudent() throws SQLException {
        Student student = createStudent(9005);

        studentDAO.addStudent(student);

        student.setFullName("Updated Student");
        student.setEmail("updated@test.com");
        student.setStudentNumber("UPDATED9005");
        student.setGroupName("IT-2302");

        boolean updated = studentDAO.updateStudent(student);

        Student result = studentDAO.findById(9005);

        assertTrue(updated);
        assertEquals("Updated Student", result.getFullName());
        assertEquals("updated@test.com", result.getEmail());
        assertEquals("UPDATED9005", result.getStudentNumber());
        assertEquals("IT-2302", result.getGroupName());
    }

    @Test
    void shouldDeleteStudent() throws SQLException {
        Student student = createStudent(9001);

        studentDAO.addStudent(student);

        boolean deleted = studentDAO.deleteStudent(9001);

        Student result = studentDAO.findById(9001);

        assertTrue(deleted);
        assertNull(result);
    }
}