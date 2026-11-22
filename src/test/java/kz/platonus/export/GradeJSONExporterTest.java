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

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradeJSONExporterTest {

    @TempDir
    Path tempDir;

    private GradeJSONExporter exporter;
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
                10301,
                "JSON Test Student",
                "json10301@example.com",
                "ST10301",
                "GROUP10301"
        );

        course = new Course(
                10301,
                "JSON Test Course",
                "JSONCOURSE10301",
                3
        );

        studentDAO.addStudent(student);
        courseDAO.addCourse(course);

        gradeDAO.addGrade(new Grade(10301, student, course, 95));

        exporter = new GradeJSONExporter();
    }

    @AfterEach
    void tearDown() throws Exception {
        cleanTestData();
    }

    private void cleanTestData() throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {

            String[] queries = {
                    "DELETE FROM grades WHERE id = 10301",
                    "DELETE FROM students WHERE id = 10301",
                    "DELETE FROM courses WHERE id = 10301",
                    "DELETE FROM users WHERE id = 10301"
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
    void testExportAllGrades() throws Exception {
        Path file = tempDir.resolve("all_grades.json");

        exporter.exportAllGrades(file.toString());

        assertTrue(Files.exists(file));
    }

    @Test
    void testExportStudentGrades() throws Exception {
        Path file = tempDir.resolve("student_grades.json");

        exporter.exportStudentGrades(10301, file.toString());

        assertTrue(Files.exists(file));
    }

    @Test
    void testJSONContainsStudentName() throws Exception {
        Path file = tempDir.resolve("student_name.json");

        exporter.exportStudentGrades(10301, file.toString());

        String content = Files.readString(file);

        assertTrue(content.contains("JSON Test Student"));
    }

    @Test
    void testJSONContainsCourseName() throws Exception {
        Path file = tempDir.resolve("course_name.json");

        exporter.exportStudentGrades(10301, file.toString());

        String content = Files.readString(file);

        assertTrue(content.contains("JSON Test Course"));
    }

    @Test
    void testJSONContainsScore() throws Exception {
        Path file = tempDir.resolve("score.json");

        exporter.exportStudentGrades(10301, file.toString());

        String content = Files.readString(file);

        assertTrue(content.contains("95"));
    }

    @Test
    void testJSONContainsCorrectFormat() throws Exception {
        Path file = tempDir.resolve("format.json");

        exporter.exportStudentGrades(10301, file.toString());

        List<String> lines = Files.readAllLines(file);

        assertTrue(lines.get(0).contains("["));
        assertTrue(lines.get(lines.size() - 1).contains("]"));
    }
}