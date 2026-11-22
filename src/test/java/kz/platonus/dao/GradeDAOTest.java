package kz.platonus.dao;

import kz.platonus.database.DatabaseInitializer;
import kz.platonus.model.Student;
import kz.platonus.model.Course;
import kz.platonus.model.Grade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradeDAOTest {

    private GradeDAO gradeDAO;
    private StudentDAO studentDAO;
    private CourseDAO courseDAO;

    private Student student;
    private Course course;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseInitializer.initialize();

        gradeDAO = new GradeDAO();
        studentDAO = new StudentDAO();
        courseDAO = new CourseDAO();

        cleanTestData();

        student = new Student(
                9201,
                "Test Student",
                "grade_test@example.com",
                "ST9201",
                "TEST-9201"
        );

        course = new Course(
                9201,
                "Test Course",
                "TEST9201",
                3
        );

        studentDAO.addStudent(student);
        courseDAO.addCourse(course);
    }

    @AfterEach
    void tearDown() throws Exception {
        cleanTestData();
    }

    private void cleanTestData() throws Exception {
        try (Connection connection =
                     kz.platonus.database.DatabaseConnection.getConnection()) {

            String[] queries = {
                    "DELETE FROM grades WHERE id BETWEEN 9201 AND 9205",
                    "DELETE FROM students WHERE id = 9201",
                    "DELETE FROM users WHERE id = 9201",
                    "DELETE FROM courses WHERE id = 9201"
            };

            for (String query : queries) {
                try (PreparedStatement statement =
                             connection.prepareStatement(query)) {
                    statement.executeUpdate();
                }
            }
        }
    }

    private Grade createGrade(int id, double score) {
        return new Grade(id, student, course, score);
    }

    @Test
    void testAddGrade() {
        Grade grade = createGrade(9201, 95);

        gradeDAO.addGrade(grade);

        Grade found = gradeDAO.findById(9201);

        assertNotNull(found);
        assertEquals(95, found.getScore());
    }

    @Test
    void testFindById() {
        gradeDAO.addGrade(createGrade(9202, 85));

        Grade found = gradeDAO.findById(9202);

        assertNotNull(found);
        assertEquals(9202, found.getId());
    }

    @Test
    void testFindAll() {
        gradeDAO.addGrade(createGrade(9203, 90));

        List<Grade> grades = gradeDAO.findAll();

        assertFalse(grades.isEmpty());
    }

    @Test
    void testUpdateGrade() {
        Grade grade = createGrade(9204, 70);

        gradeDAO.addGrade(grade);

        grade.setScore(95);
        gradeDAO.updateGrade(grade);

        Grade updated = gradeDAO.findById(9204);

        assertNotNull(updated);
        assertEquals(95, updated.getScore());
    }

    @Test
    void testDeleteGrade() {
        gradeDAO.addGrade(createGrade(9205, 80));

        gradeDAO.deleteGrade(9205);

        assertNull(gradeDAO.findById(9205));
    }

    @Test
    void testFindByStudentId() {
        gradeDAO.addGrade(createGrade(9201, 88));

        List<Grade> grades = gradeDAO.findByStudentId(9201);

        assertFalse(grades.isEmpty());
    }

    @Test
    void testFindByCourseId() {
        gradeDAO.addGrade(createGrade(9202, 92));

        List<Grade> grades = gradeDAO.findByCourseId(9201);

        assertFalse(grades.isEmpty());
    }
}