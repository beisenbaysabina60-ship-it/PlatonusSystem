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

import static org.junit.jupiter.api.Assertions.*;

class GradeServiceTest {

    private GradeService gradeService;
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

        student = new Student(
                9901,
                "Grade Test Student",
                "grade_student9901@example.com",
                "ST9901",
                "GROUP9901"
        );

        course1 = new Course(
                9901,
                "Mathematics",
                "MATH9901",
                3
        );

        course2 = new Course(
                9902,
                "Programming",
                "PROG9902",
                3
        );

        course3 = new Course(
                9903,
                "Database",
                "DB9903",
                3
        );

        studentDAO.addStudent(student);
        courseDAO.addCourse(course1);
        courseDAO.addCourse(course2);
        courseDAO.addCourse(course3);

        gradeService = new GradeService();
    }

    @AfterEach
    void tearDown() throws Exception {
        cleanTestData();
    }

    private void cleanTestData() throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {

            String[] queries = {
                    "DELETE FROM grades WHERE student_id = 9901 OR id BETWEEN 9901 AND 9905",
                    "DELETE FROM students WHERE id = 9901",
                    "DELETE FROM courses WHERE id BETWEEN 9901 AND 9903",
                    "DELETE FROM users WHERE id = 9901"
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
    void testAddGrade() {
        Grade grade = new Grade(
                9901,
                student,
                course1,
                95
        );

        gradeService.addGrade(grade);

        Grade found = new GradeDAO().findById(9901);

        assertNotNull(found);
        assertEquals(95, found.getScore());
    }

    @Test
    void testUpdateGrade() {
        Grade grade = new Grade(
                9902,
                student,
                course1,
                70
        );

        gradeService.addGrade(grade);

        gradeService.updateGrade(9902, 90);

        Grade updated = new GradeDAO().findById(9902);

        assertNotNull(updated);
        assertEquals(90, updated.getScore());
    }

    @Test
    void testGetStudentGrades() {
        gradeService.addGrade(
                new Grade(9901, student, course1, 85)
        );

        assertFalse(
                gradeService.getStudentGrades(9901).isEmpty()
        );
    }

    @Test
    void testGetCourseGrades() {
        gradeService.addGrade(
                new Grade(9901, student, course1, 85)
        );

        assertFalse(
                gradeService.getCourseGrades(9901).isEmpty()
        );
    }

    @Test
    void testCalculateAverageGrade() {
        gradeService.addGrade(
                new Grade(9901, student, course1, 80)
        );

        gradeService.addGrade(
                new Grade(9902, student, course2, 90)
        );

        gradeService.addGrade(
                new Grade(9903, student, course3, 100)
        );

        double average =
                gradeService.calculateAverageGrade(9901);

        assertEquals(90.0, average, 0.01);
    }

    @Test
    void testCountGrades() {
        gradeService.addGrade(
                new Grade(9901, student, course1, 80)
        );

        gradeService.addGrade(
                new Grade(9902, student, course2, 90)
        );

        assertEquals(2, gradeService.countGrades(9901));
    }

    @Test
    void testGetHighestGrade() {
        gradeService.addGrade(
                new Grade(9901, student, course1, 70)
        );

        gradeService.addGrade(
                new Grade(9902, student, course2, 95)
        );

        gradeService.addGrade(
                new Grade(9903, student, course3, 85)
        );

        assertEquals(95.0, gradeService.getHighestGrade(9901));
    }

    @Test
    void testGetLowestGrade() {
        gradeService.addGrade(
                new Grade(9901, student, course1, 70)
        );

        gradeService.addGrade(
                new Grade(9902, student, course2, 95)
        );

        gradeService.addGrade(
                new Grade(9903, student, course3, 85)
        );

        assertEquals(70.0, gradeService.getLowestGrade(9901));
    }

    @Test
    void testAddGradeWithNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> gradeService.addGrade(null)
        );
    }

   @Test
void testAddGradeWithInvalidScore() {
    assertThrows(
            IllegalArgumentException.class,
            () -> {
                Grade grade = new Grade(
                        9904,
                        student,
                        course1,
                        150
                );

                gradeService.addGrade(grade);
            }
    );
}

    @Test
    void testUpdateNonExistingGrade() {
        assertThrows(
                IllegalArgumentException.class,
                () -> gradeService.updateGrade(99999, 90)
        );
    }

    @Test
    void testUpdateGradeWithInvalidScore() {
        gradeService.addGrade(
                new Grade(9905, student, course1, 80)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> gradeService.updateGrade(9905, 150)
        );
    }
}