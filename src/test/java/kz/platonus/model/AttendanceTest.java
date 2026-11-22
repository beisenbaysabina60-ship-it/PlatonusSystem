package kz.platonus.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class AttendanceTest {

    private Student createStudent() {
        return new Student(1, "Aizhan Kanat", "aizhan@test.com",
                "ST001", "IT-2301");
    }

    private Lesson createLesson() {
        Course course = new Course(1, "Java Programming", "JAVA101", 5);
        Teacher teacher = new Teacher(2, "Teacher Name", "teacher@test.com",
                "Computer Science", "Master");
        StudentGroup group = new StudentGroup(1, "IT-2301", "IT");

        return new Lesson(
                1,
                course,
                teacher,
                group,
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                "Room 101"
        );
    }

    @Test
    void shouldCreateAttendanceSuccessfully() {
        Attendance attendance = new Attendance(
                1, createStudent(), createLesson(),
                AttendanceStatus.PRESENT, "Present"
        );

        assertEquals(1, attendance.getId());
        assertEquals(AttendanceStatus.PRESENT, attendance.getStatus());
        assertEquals("Present", attendance.getComment());
    }

    @Test
    void shouldRejectInvalidAttendanceId() {
        assertThrows(IllegalArgumentException.class,
                () -> new Attendance(0, createStudent(), createLesson(),
                        AttendanceStatus.PRESENT, ""));
    }

    @Test
    void shouldRejectNullStudent() {
        assertThrows(IllegalArgumentException.class,
                () -> new Attendance(1, null, createLesson(),
                        AttendanceStatus.PRESENT, ""));
    }

    @Test
    void shouldUpdateAttendanceStatus() {
        Attendance attendance = new Attendance(
                1, createStudent(), createLesson(),
                AttendanceStatus.PRESENT, ""
        );

        attendance.setStatus(AttendanceStatus.LATE);

        assertEquals(AttendanceStatus.LATE, attendance.getStatus());
    }

    @Test
    void shouldUpdateComment() {
        Attendance attendance = new Attendance(
                1, createStudent(), createLesson(),
                AttendanceStatus.ABSENT, ""
        );

        attendance.setComment("Medical reason");

        assertEquals("Medical reason", attendance.getComment());
    }
}