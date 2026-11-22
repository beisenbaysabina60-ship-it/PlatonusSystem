package kz.platonus.dao;

import kz.platonus.database.DatabaseInitializer;
import kz.platonus.database.DatabaseConnection;
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

class AttendanceDAOTest {

    private AttendanceDAO attendanceDAO;
    private Student student;
    private Lesson lesson;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseInitializer.initialize();

        cleanTestData();

        StudentDAO studentDAO = new StudentDAO();
        TeacherDAO teacherDAO = new TeacherDAO();
        CourseDAO courseDAO = new CourseDAO();
        StudentGroupDAO groupDAO = new StudentGroupDAO();
        LessonDAO lessonDAO = new LessonDAO();

        student = new Student(
                9301,
                "Test Student",
                "attendance_test@example.com",
                "ST9301",
                "TEST-9301"
        );

        Teacher teacher = new Teacher(
                9302,
                "Test Teacher",
                "teacher_test@example.com",
                "Computer Science",
                "Master"
        );

        Course course = new Course(
                9301,
                "Test Course",
                "TEST9301",
                3
        );

        StudentGroup group = new StudentGroup(
                9301,
                "TEST-9301",
                "Computer Science"
        );

        studentDAO.addStudent(student);
        teacherDAO.addTeacher(teacher);
        courseDAO.addCourse(course);
        groupDAO.addGroup(group);

        lesson = new Lesson(
                9301,
                course,
                teacher,
                group,
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                "Room 101"
        );

        lessonDAO.addLesson(lesson);

        attendanceDAO = new AttendanceDAO();
    }

    @AfterEach
    void tearDown() throws Exception {
        cleanTestData();
    }

    private void cleanTestData() throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {

            String[] queries = {
                    "DELETE FROM attendance WHERE id BETWEEN 9301 AND 9305",
                    "DELETE FROM grades WHERE student_id = 9301 OR course_id = 9301",
                    "DELETE FROM lessons WHERE id = 9301",
                    "DELETE FROM students WHERE id = 9301",
                    "DELETE FROM teachers WHERE id = 9302",
                    "DELETE FROM admins WHERE id = 9303",
                    "DELETE FROM student_groups WHERE id = 9301",
                    "DELETE FROM courses WHERE id = 9301",
                    "DELETE FROM users WHERE id IN (9301, 9302, 9303)"
            };

            for (String query : queries) {
                try (PreparedStatement statement =
                             connection.prepareStatement(query)) {
                    statement.executeUpdate();
                }
            }
        }
    }

    private Attendance createAttendance(int id, AttendanceStatus status) {
        return new Attendance(
                id,
                student,
                lesson,
                status,
                "Test comment"
        );
    }

    @Test
    void testAddAttendance() {
        Attendance attendance =
                createAttendance(9301, AttendanceStatus.PRESENT);

        attendanceDAO.addAttendance(attendance);

        Attendance found = attendanceDAO.findById(9301);

        assertNotNull(found);
        assertEquals(AttendanceStatus.PRESENT, found.getStatus());
    }

    @Test
    void testFindById() {
        attendanceDAO.addAttendance(
                createAttendance(9302, AttendanceStatus.ABSENT)
        );

        Attendance found = attendanceDAO.findById(9302);

        assertNotNull(found);
        assertEquals(9302, found.getId());
    }

    @Test
    void testFindAll() {
        attendanceDAO.addAttendance(
                createAttendance(9303, AttendanceStatus.PRESENT)
        );

        List<Attendance> attendances = attendanceDAO.findAll();

        assertFalse(attendances.isEmpty());
    }

    @Test
    void testUpdateAttendance() {
        Attendance attendance =
                createAttendance(9304, AttendanceStatus.ABSENT);

        attendanceDAO.addAttendance(attendance);

        attendance.setStatus(AttendanceStatus.PRESENT);
        attendance.setComment("Updated comment");

        attendanceDAO.updateAttendance(attendance);

        Attendance updated = attendanceDAO.findById(9304);

        assertNotNull(updated);
        assertEquals(AttendanceStatus.PRESENT, updated.getStatus());
        assertEquals("Updated comment", updated.getComment());
    }

    @Test
    void testDeleteAttendance() {
        attendanceDAO.addAttendance(
                createAttendance(9305, AttendanceStatus.LATE)
        );

        attendanceDAO.deleteAttendance(9305);

        assertNull(attendanceDAO.findById(9305));
    }

    @Test
    void testFindByStudentId() {
        attendanceDAO.addAttendance(
                createAttendance(9301, AttendanceStatus.PRESENT)
        );

        List<Attendance> attendances =
                attendanceDAO.findByStudentId(9301);

        assertFalse(attendances.isEmpty());
    }

    @Test
    void testFindByLessonId() {
        attendanceDAO.addAttendance(
                createAttendance(9302, AttendanceStatus.LATE)
        );

        List<Attendance> attendances =
                attendanceDAO.findByLessonId(9301);

        assertFalse(attendances.isEmpty());
    }
}