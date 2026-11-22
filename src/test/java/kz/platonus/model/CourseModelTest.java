package kz.platonus.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseModelTest {

    @Test
    void shouldCreateCourseSuccessfully() {
        Course course = new Course(1, "Java Programming", "JAVA101", 5);

        assertEquals(1, course.getId());
        assertEquals("Java Programming", course.getCourseName());
        assertEquals("JAVA101", course.getCourseCode());
        assertEquals(5, course.getCredits());
    }

    @Test
    void shouldRejectInvalidCourseId() {
        assertThrows(IllegalArgumentException.class,
                () -> new Course(0, "Java", "JAVA101", 5));
    }

    @Test
    void shouldRejectEmptyCourseName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Course(1, "", "JAVA101", 5));
    }

    @Test
    void shouldRejectInvalidCredits() {
        assertThrows(IllegalArgumentException.class,
                () -> new Course(1, "Java", "JAVA101", 0));
    }

    @Test
    void shouldUpdateCourseName() {
        Course course = new Course(1, "Java", "JAVA101", 5);

        course.setCourseName("Advanced Java");

        assertEquals("Advanced Java", course.getCourseName());
    }
}