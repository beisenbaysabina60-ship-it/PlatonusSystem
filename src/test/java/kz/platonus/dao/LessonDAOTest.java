package kz.platonus.dao;

import kz.platonus.database.DatabaseConnection;
import kz.platonus.database.DatabaseInitializer;
import kz.platonus.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LessonDAOTest {

    private LessonDAO lessonDAO;
    private Lesson lesson;
    private Course course;
    private Teacher teacher;
    private StudentGroup group;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseInitializer.initialize();
        cleanTestData();

        CourseDAO courseDAO = new CourseDAO();
        TeacherDAO teacherDAO = new TeacherDAO();
        StudentGroupDAO groupDAO = new StudentGroupDAO();

        course = new Course(9401, "Test Lesson Course", "TEST9401", 3);
        teacher = new Teacher(
                9402,
                "Test Lesson Teacher",
                "lesson_teacher_test@example.com",
                "Computer Science",
                "Master"
        );
        group = new StudentGroup(9401, "TEST-9401", "Computer Science");

        courseDAO.addCourse(course);
        teacherDAO.addTeacher(teacher);
        groupDAO.addGroup(group);

        lesson = new Lesson(
                9401,
                course,
                teacher,
                group,
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                "Room 101"
        );

        lessonDAO = new LessonDAO();
    }

    @AfterEach
    void tearDown() throws Exception {
        cleanTestData();
    }

    private void cleanTestData() throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {

            String[] queries = {
                    "DELETE FROM attendance WHERE lesson_id = 9401",
                    "DELETE FROM grades WHERE course_id = 9401",
                    "DELETE FROM lessons WHERE id BETWEEN 9401 AND 9405",
                    "DELETE FROM teachers WHERE id = 9402",
                    "DELETE FROM student_groups WHERE id = 9401",
                    "DELETE FROM courses WHERE id = 9401",
                    "DELETE FROM users WHERE id = 9402"
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
    void testAddLesson() {
        lessonDAO.addLesson(lesson);

        Lesson found = lessonDAO.findById(9401);

        assertNotNull(found);
        assertEquals(9401, found.getId());
        assertEquals("Test Lesson Course", found.getCourse().getCourseName());
    }

    @Test
    void testFindById() {
        lessonDAO.addLesson(lesson);

        Lesson found = lessonDAO.findById(9401);

        assertNotNull(found);
        assertEquals("Room 101", found.getRoom());
    }

    @Test
    void testFindAll() {
        lessonDAO.addLesson(lesson);

        List<Lesson> lessons = lessonDAO.findAll();

        assertFalse(lessons.isEmpty());
    }

    @Test
    void testUpdateLesson() {
        lessonDAO.addLesson(lesson);

        Lesson updatedLesson = new Lesson(
                9401,
                course,
                teacher,
                group,
                LocalDate.now(),
                LocalTime.of(11, 0),
                LocalTime.of(12, 0),
                "Room 202"
        );

        lessonDAO.updateLesson(updatedLesson);

        Lesson found = lessonDAO.findById(9401);

        assertNotNull(found);
        assertEquals("Room 202", found.getRoom());
        assertEquals(LocalTime.of(11, 0), found.getStartTime());
    }

    @Test
    void testDeleteLesson() {
        lessonDAO.addLesson(lesson);

        lessonDAO.deleteLesson(9401);

        assertNull(lessonDAO.findById(9401));
    }
}