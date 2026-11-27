package kz.platonus.schedule;

import kz.platonus.model.Course;
import kz.platonus.model.Lesson;
import kz.platonus.model.StudentGroup;
import kz.platonus.model.Teacher;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleGeneratorTest {
    private final Course course = new Course(1, "Math", "MATH1", 3);
    private final Teacher teacher = new Teacher(1, "Teacher One", "t1@example.com", "Math", "Master");
    private final StudentGroup group = new StudentGroup(1, "CS-1", "CS");
    private final ScheduleGenerator generator = new ScheduleGenerator();

    @Test
    void generatesLessonForWeekday() {
        List<Lesson> result = generator.generate(
                List.of(new ScheduleRequest(course, teacher, group, 90)),
                List.of(), LocalDate.of(2026, 11, 30), 5,
                List.of("101", "102"), 100);
        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getId());
        assertEquals(90, java.time.Duration.between(result.get(0).getStartTime(),
                result.get(0).getEndTime()).toMinutes());
    }

    @Test
    void placesSameTeacherLessonsInDifferentTimeSlots() {
        List<ScheduleRequest> requests = List.of(
                new ScheduleRequest(course, teacher, group, 60),
                new ScheduleRequest(course, teacher, group, 60));
        List<Lesson> result = generator.generate(requests, List.of(),
                LocalDate.of(2026, 11, 30), 1, List.of("101"), 200);
        assertEquals(2, result.size());
        assertFalse(result.get(0).getStartTime().equals(result.get(1).getStartTime())
                && result.get(0).getDate().equals(result.get(1).getDate()));
    }

    @Test
    void failsWhenNoWeekdayIsAvailable() {
        assertThrows(IllegalStateException.class, () -> generator.generate(
                List.of(new ScheduleRequest(course, teacher, group, 60)),
                List.of(), LocalDate.of(2026, 11, 28), 2,
                List.of("101"), 300));
    }
}
