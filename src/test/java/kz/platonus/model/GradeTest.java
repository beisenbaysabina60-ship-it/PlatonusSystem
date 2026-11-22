package kz.platonus.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradeTest {

    private Student createStudent() {
        return new Student(1, "Aizhan Kanat", "aizhan@test.com",
                "ST001", "IT-2301");
    }

    private Course createCourse() {
        return new Course(1, "Java Programming", "JAVA101", 5);
    }

    @Test
    void shouldCreateGradeSuccessfully() {
        Grade grade = new Grade(1, createStudent(), createCourse(), 95);

        assertEquals(95, grade.getScore());
        assertEquals("A", grade.getLetterGrade());
    }

    @Test
    void shouldCalculateLetterGradeCorrectly() {
        Grade grade = new Grade(1, createStudent(), createCourse(), 75);

        assertEquals("B-", grade.getLetterGrade());
    }

    @Test
    void shouldRejectScoreAbove100() {
        assertThrows(IllegalArgumentException.class,
                () -> new Grade(1, createStudent(), createCourse(), 101));
    }

    @Test
    void shouldRejectNegativeScore() {
        assertThrows(IllegalArgumentException.class,
                () -> new Grade(1, createStudent(), createCourse(), -1));
    }

    @Test
    void shouldUpdateScoreAndLetterGrade() {
        Grade grade = new Grade(1, createStudent(), createCourse(), 50);

        grade.setScore(90);

        assertEquals(90, grade.getScore());
        assertEquals("A-", grade.getLetterGrade());
    }
}