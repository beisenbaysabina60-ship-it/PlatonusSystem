package kz.platonus.dao;

import kz.platonus.database.DatabaseConnection;
import kz.platonus.database.DatabaseInitializer;
import kz.platonus.model.Course;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseDAOTest {

    private final CourseDAO courseDAO = new CourseDAO();

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
        int[] ids = {9101, 9102, 9103, 9104, 9105};

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            for (int id : ids) {
                statement.executeUpdate(
                        "DELETE FROM grades WHERE course_id = " + id);
                statement.executeUpdate(
                        "DELETE FROM lessons WHERE course_id = " + id);
                statement.executeUpdate(
                        "DELETE FROM courses WHERE id = " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Test cleanup failed", e);
        }
    }

    private Course createCourse(int id) {
        return new Course(
                id,
                "Test Course " + id,
                "TEST" + id,
                5
        );
    }

    @Test
    void shouldAddCourse() throws SQLException {
        Course course = createCourse(9101);

        courseDAO.addCourse(course);

        Course saved = courseDAO.findById(9101);

        assertNotNull(saved);
        assertEquals("Test Course 9101", saved.getCourseName());
        assertEquals("TEST9101", saved.getCourseCode());
        assertEquals(5, saved.getCredits());
    }

    @Test
    void shouldFindCourseById() throws SQLException {
        courseDAO.addCourse(createCourse(9102));

        Course found = courseDAO.findById(9102);

        assertNotNull(found);
        assertEquals(9102, found.getId());
    }

    @Test
    void shouldReturnAllCourses() throws SQLException {
        courseDAO.addCourse(createCourse(9103));
        courseDAO.addCourse(createCourse(9104));

        List<Course> courses = courseDAO.findAll();

        assertTrue(courses.stream()
                .anyMatch(course -> course.getId() == 9103));

        assertTrue(courses.stream()
                .anyMatch(course -> course.getId() == 9104));
    }

    @Test
    void shouldUpdateCourse() throws SQLException {
        Course course = createCourse(9105);
        courseDAO.addCourse(course);

        course.setCourseName("Updated Course");
        course.setCourseCode("UPDATED9105");
        course.setCredits(6);

        boolean updated = courseDAO.updateCourse(course);

        Course result = courseDAO.findById(9105);

        assertTrue(updated);
        assertEquals("Updated Course", result.getCourseName());
        assertEquals("UPDATED9105", result.getCourseCode());
        assertEquals(6, result.getCredits());
    }

    @Test
    void shouldDeleteCourse() throws SQLException {
        courseDAO.addCourse(createCourse(9101));

        boolean deleted = courseDAO.deleteCourse(9101);

        assertTrue(deleted);
        assertNull(courseDAO.findById(9101));
    }
}