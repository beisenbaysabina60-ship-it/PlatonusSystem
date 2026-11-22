package kz.platonus.dao;

import kz.platonus.database.DatabaseConnection;
import kz.platonus.database.DatabaseInitializer;
import kz.platonus.model.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeacherDAOTest {

    private TeacherDAO teacherDAO;
    private Teacher teacher;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseInitializer.initialize();
        cleanTestData();

        teacherDAO = new TeacherDAO();

        teacher = new Teacher(
                9501,
                "Test Teacher",
                "teacher9501@example.com",
                "Computer Science",
                "Master"
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        cleanTestData();
    }

    private void cleanTestData() throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {

            String[] queries = {
                    "DELETE FROM lessons WHERE teacher_id = 9501",
                    "DELETE FROM teachers WHERE id = 9501",
                    "DELETE FROM users WHERE id = 9501"
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
    void testAddTeacher() {
        teacherDAO.addTeacher(teacher);

        Teacher found = teacherDAO.findById(9501);

        assertNotNull(found);
        assertEquals("Test Teacher", found.getFullName());
    }

    @Test
    void testFindById() {
        teacherDAO.addTeacher(teacher);

        Teacher found = teacherDAO.findById(9501);

        assertNotNull(found);
        assertEquals("Computer Science", found.getDepartment());
    }

    @Test
    void testFindAll() {
        teacherDAO.addTeacher(teacher);

        List<Teacher> teachers = teacherDAO.findAll();

        assertFalse(teachers.isEmpty());
    }

    @Test
    void testUpdateTeacher() {
        teacherDAO.addTeacher(teacher);

        Teacher updatedTeacher = new Teacher(
                9501,
                "Updated Teacher",
                "teacher9501@example.com",
                "Mathematics",
                "PhD"
        );

        teacherDAO.updateTeacher(updatedTeacher);

        Teacher found = teacherDAO.findById(9501);

        assertNotNull(found);
        assertEquals("Updated Teacher", found.getFullName());
        assertEquals("Mathematics", found.getDepartment());
        assertEquals("PhD", found.getAcademicDegree());
    }

    @Test
    void testDeleteTeacher() {
        teacherDAO.addTeacher(teacher);

        teacherDAO.deleteTeacher(9501);

        assertNull(teacherDAO.findById(9501));
    }
}