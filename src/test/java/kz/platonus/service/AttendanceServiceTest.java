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
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class AttendanceServiceTest {

    private AttendanceService attendanceService;

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
                9801,
                "Service Test Student",
                "service_student9801@example.com",
                "ST9801",
                "GROUP9801"
        );

        Teacher teacher = new Teacher(
                9802,
                "Service Test Teacher",
                "service_teacher9802@example.com",
                "Computer Science",
                "Master"
        );

        Course course = new Course(
                9801,
                "Service Test Course",
                "SERVICE9801",
                3
        );

        StudentGroup group = new StudentGroup(
                9801,
                "GROUP9801",
                "Computer Science"
        );

        studentDAO.addStudent(student);
        teacherDAO.addTeacher(teacher);
        courseDAO.addCourse(course);
        groupDAO.addGroup(group);

        lesson = new Lesson(
                9801,
                course,
                teacher,
                group,
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                "Room 101"
        );

        lessonDAO.addLesson(lesson);

        attendanceService = new AttendanceService();
    }

    @AfterEach
    void tearDown() throws Exception {
        cleanTestData();
    }

    private void cleanTestData() throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {

            String[] queries = {
                    "DELETE FROM attendance WHERE id BETWEEN 9801 AND 9805",
                    "DELETE FROM grades WHERE student_id = 9801 OR course_id = 9801",
                    "DELETE FROM lessons WHERE id BETWEEN 9801 AND 9803",
                    "DELETE FROM students WHERE id = 9801",
                    "DELETE FROM teachers WHERE id = 9802",
                    "DELETE FROM student_groups WHERE id = 9801",
                    "DELETE FROM courses WHERE id = 9801",
                    "DELETE FROM users WHERE id IN (9801, 9802)"
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
    void testRecordAttendance() {
        Attendance attendance = new Attendance(
                9801,
                student,
                lesson,
                AttendanceStatus.PRESENT,
                "Present"
        );

        attendanceService.recordAttendance(attendance);

        assertNotNull(new AttendanceDAO().findById(9801));
    }

    @Test
    void testUpdateAttendanceStatus() {
        Attendance attendance = new Attendance(
                9802,
                student,
                lesson,
                AttendanceStatus.ABSENT,
                "Absent"
        );

        attendanceService.recordAttendance(attendance);

        attendanceService.updateAttendanceStatus(
                9802,
                AttendanceStatus.PRESENT
        );

        Attendance updated = new AttendanceDAO().findById(9802);

        assertNotNull(updated);
        assertEquals(
                AttendanceStatus.PRESENT,
                updated.getStatus()
        );
    }

    @Test
    void testGetStudentAttendance() {
        Attendance attendance = new Attendance(
                9803,
                student,
                lesson,
                AttendanceStatus.PRESENT,
                "Test"
        );

        attendanceService.recordAttendance(attendance);

        assertFalse(
                attendanceService.getStudentAttendance(9801).isEmpty()
        );
    }

    @Test
    void testGetLessonAttendance() {
        Attendance attendance = new Attendance(
                9804,
                student,
                lesson,
                AttendanceStatus.LATE,
                "Late"
        );

        attendanceService.recordAttendance(attendance);

        assertFalse(
                attendanceService.getLessonAttendance(9801).isEmpty()
        );
    }

    @Test
    void testCalculateAttendancePercentage() {

        LessonDAO lessonDAO = new LessonDAO();

        Lesson lesson2 = new Lesson(
                9802,
                lesson.getCourse(),
                lesson.getTeacher(),
                lesson.getGroup(),
                LocalDate.now(),
                LocalTime.of(11, 0),
                LocalTime.of(12, 0),
                "Room 102"
        );

        Lesson lesson3 = new Lesson(
                9803,
                lesson.getCourse(),
                lesson.getTeacher(),
                lesson.getGroup(),
                LocalDate.now(),
                LocalTime.of(13, 0),
                LocalTime.of(14, 0),
                "Room 103"
        );

        lessonDAO.addLesson(lesson2);
        lessonDAO.addLesson(lesson3);

        attendanceService.recordAttendance(
                new Attendance(
                        9801,
                        student,
                        lesson,
                        AttendanceStatus.PRESENT,
                        "Present"
                )
        );

        attendanceService.recordAttendance(
                new Attendance(
                        9802,
                        student,
                        lesson2,
                        AttendanceStatus.LATE,
                        "Late"
                )
        );

        attendanceService.recordAttendance(
                new Attendance(
                        9803,
                        student,
                        lesson3,
                        AttendanceStatus.ABSENT,
                        "Absent"
                )
        );

        double percentage =
                attendanceService.calculateAttendancePercentage(9801);

        assertEquals(66.67, percentage, 0.1);
    }

    @Test
    void testCountByStatus() {
        attendanceService.recordAttendance(
                new Attendance(
                        9804,
                        student,
                        lesson,
                        AttendanceStatus.PRESENT,
                        "Present"
                )
        );

        long count = attendanceService.countByStatus(
                9801,
                AttendanceStatus.PRESENT
        );

        assertEquals(1, count);
    }

    @Test
    void testRecordAttendanceWithNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> attendanceService.recordAttendance(null)
        );
    }

    @Test
    void testUpdateNonExistingAttendance() {
        assertThrows(
                IllegalArgumentException.class,
                () -> attendanceService.updateAttendanceStatus(
                        99999,
                        AttendanceStatus.PRESENT
                )
        );
    }
}