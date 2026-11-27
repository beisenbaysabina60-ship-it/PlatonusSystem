package kz.platonus.schedule;

import kz.platonus.model.Course;
import kz.platonus.model.Lesson;
import kz.platonus.model.StudentGroup;
import kz.platonus.model.Teacher;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleConflictServiceTest {
    private final ScheduleConflictService service = new ScheduleConflictService();
    private final LocalDate date = LocalDate.of(2026, 12, 1);
    private final Course course = new Course(1, "Math", "MATH1", 3);
    private final Teacher teacher1 = new Teacher(1, "Teacher One", "t1@example.com", "Math", "Master");
    private final Teacher teacher2 = new Teacher(2, "Teacher Two", "t2@example.com", "Math", "Master");
    private final StudentGroup group1 = new StudentGroup(1, "CS-1", "CS");
    private final StudentGroup group2 = new StudentGroup(2, "CS-2", "CS");

    private Lesson lesson(int id, Teacher teacher, StudentGroup group, String room,
                          int startHour, int endHour) {
        return new Lesson(id, course, teacher, group, date,
                LocalTime.of(startHour, 0), LocalTime.of(endHour, 0), room);
    }

    @Test
    void detectsTeacherConflict() {
        Lesson existing = lesson(1, teacher1, group1, "101", 9, 10);
        Lesson candidate = lesson(2, teacher1, group2, "102", 9, 10);
        assertTrue(service.findConflicts(candidate, List.of(existing))
                .contains("Teacher is already assigned to another lesson"));
    }

    @Test
    void detectsGroupAndRoomConflicts() {
        Lesson existing = lesson(1, teacher1, group1, "101", 9, 10);
        Lesson candidate = lesson(2, teacher2, group1, "101", 9, 10);
        List<String> conflicts = service.findConflicts(candidate, List.of(existing));
        assertEquals(2, conflicts.size());
        assertFalse(service.isAvailable(candidate, List.of(existing)));
    }

    @Test
    void allowsAdjacentLessonsAndDifferentDate() {
        Lesson existing = lesson(1, teacher1, group1, "101", 9, 10);
        Lesson adjacent = lesson(2, teacher1, group2, "101", 10, 11);
        assertTrue(service.isAvailable(adjacent, List.of(existing)));
    }
}
