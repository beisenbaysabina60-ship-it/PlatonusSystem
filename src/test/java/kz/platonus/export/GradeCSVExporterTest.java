package kz.platonus.export;

import kz.platonus.dao.CourseDAO;
import kz.platonus.dao.GradeDAO;
import kz.platonus.dao.StudentDAO;
import kz.platonus.database.DatabaseConnection;
import kz.platonus.database.DatabaseInitializer;
import kz.platonus.model.Course;
import kz.platonus.model.Grade;
import kz.platonus.model.Student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradeCSVExporterTest {

    @TempDir
    Path tempDir;

    private GradeCSVExporter exporter;
    private Student student;
    private Course course;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseInitializer.initialize();
        cleanTestData();

        StudentDAO studentDAO = new StudentDAO();
        CourseDAO courseDAO = new CourseDAO();
        GradeDAO gradeDAO = new GradeDAO();

        student = new Student(
                10201,
                "CSV Test Student",
                "csv10201@example.com",
                "ST10201",
                "GROUP10201"
        );

        course = new Course(
                10201,
                "CSV Test Course",
                "CSVCOURSE10201",
                3
        );

        studentDAO.addStudent(student);
        courseDAO.addCourse(course);

        gradeDAO.addGrade(
                new Grade(10201, student, course, 95)
        );

        exporter = new GradeCSVExporter();
    }

    @AfterEach
    void tearDown() throws Exception {
        cleanTestData();
    }

    private void cleanTestData() throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {

            String[] queries = {
                    "DELETE FROM grades WHERE id = 10201",
                    "DELETE FROM students WHERE id = 10201",
                    "DELETE FROM courses WHERE id = 10201",
                    "DELETE FROM users WHERE id = 10201"
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
    void testExportAllGrades() throws IOException {
        Path file = tempDir.resolve("all_grades.csv");

        exporter.exportAllGrades(file.toString());

        assertTrue(Files.exists(file));
    }

    @Test
    void testExportStudentGrades() throws IOException {
        Path file = tempDir.resolve("student_grades.csv");

        exporter.exportStudentGrades(10201, file.toString());

        assertTrue(Files.exists(file));
    }

    @Test
    void testCSVContainsHeader() throws IOException {
        Path file = tempDir.resolve("header_test.csv");

        exporter.exportAllGrades(file.toString());

        List<String> lines = Files.readAllLines(file);

        assertFalse(lines.isEmpty());
        assertEquals(
                "ID,Student,Course,Score,Letter Grade",
                lines.get(0)
        );
    }

    @Test
    void testCSVContainsStudentData() throws IOException {
        Path file = tempDir.resolve("student_data.csv");

        exporter.exportStudentGrades(10201, file.toString());

        List<String> lines = Files.readAllLines(file);

        assertTrue(lines.stream().anyMatch(
                line -> line.contains("CSV Test Student")
        ));
    }

    @Test
    void testCSVContainsScore() throws IOException {
        Path file = tempDir.resolve("score_test.csv");

        exporter.exportStudentGrades(10201, file.toString());

        List<String> lines = Files.readAllLines(file);

        assertTrue(lines.stream().anyMatch(
                line -> line.contains("95")
        ));
    }
}