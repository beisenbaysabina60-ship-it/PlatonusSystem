package kz.platonus.service;

import kz.platonus.dao.*;
import kz.platonus.database.DatabaseConnection;
import kz.platonus.database.DatabaseInitializer;
import kz.platonus.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradeStatisticsServiceTest {

    private GradeStatisticsService statisticsService;

    private Student student;
    private Course course1;
    private Course course2;
    private Course course3;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseInitializer.initialize();
        cleanTestData();

        StudentDAO studentDAO = new StudentDAO();
        CourseDAO courseDAO = new CourseDAO();
        GradeDAO gradeDAO = new GradeDAO();

        student = new Student(
                10101,
                "Statistics Test Student",
                "statistics10101@example.com",
                "ST10101",
                "GROUP10101"
        );

        course1 = new Course(
                10101,
                "Mathematics",
                "STATMATH10101",
                3
        );

        course2 = new Course(
                10102,
                "Programming",
                "STATPROG10102",
                3
        );

        course3 = new Course(
                10103,
                "Database",
                "STATDB10103",
                3
        );

        studentDAO.addStudent(student);
        courseDAO.addCourse(course1);
        courseDAO.addCourse(course2);
        courseDAO.addCourse(course3);

        gradeDAO.addGrade(new Grade(10101, student, course1, 95));
        gradeDAO.addGrade(new Grade(10102, student, course2, 70));
        gradeDAO.addGrade(new Grade(10103, student, course3, 40));

        statisticsService = new GradeStatisticsService();
    }

    @AfterEach
    void tearDown() throws Exception {
        cleanTestData();
    }

    private void cleanTestData() throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {

            String[] queries = {
                    "DELETE FROM grades WHERE id BETWEEN 10101 AND 10103",
                    "DELETE FROM students WHERE id = 10101",
                    "DELETE FROM courses WHERE id BETWEEN 10101 AND 10103",
                    "DELETE FROM users WHERE id = 10101"
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
    void testGetExcellentGrades() {
        List<Grade> grades =
                statisticsService.getExcellentGrades(10101);

        assertEquals(1, grades.size());
        assertEquals(95, grades.get(0).getScore());
    }

    @Test
    void testGetFailedGrades() {
        List<Grade> grades =
                statisticsService.getFailedGrades(10101);

        assertEquals(1, grades.size());
        assertEquals(40, grades.get(0).getScore());
    }

    @Test
    void testGetGradesSortedAscending() {
        List<Grade> grades =
                statisticsService.getGradesSortedAscending(10101);

        assertEquals(40, grades.get(0).getScore());
        assertEquals(70, grades.get(1).getScore());
        assertEquals(95, grades.get(2).getScore());
    }

    @Test
    void testGetGradesSortedDescending() {
        List<Grade> grades =
                statisticsService.getGradesSortedDescending(10101);

        assertEquals(95, grades.get(0).getScore());
        assertEquals(70, grades.get(1).getScore());
        assertEquals(40, grades.get(2).getScore());
    }

    @Test
    void testCalculateAverage() {
        double average =
                statisticsService.calculateAverage(10101);

        assertEquals(68.33, average, 0.1);
    }

    @Test
    void testCalculateTotal() {
        double total =
                statisticsService.calculateTotal(10101);

        assertEquals(205.0, total);
    }

    @Test
    void testCountGrades() {
        long count =
                statisticsService.countGrades(10101);

        assertEquals(3, count);
    }
}