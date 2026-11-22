package kz.platonus.concurrency;

import kz.platonus.model.Course;
import kz.platonus.model.Grade;
import kz.platonus.model.Student;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradeProcessingTaskTest {

    private Student createStudent() {
        return new Student(
                11001,
                "Concurrency Test Student",
                "concurrency@example.com",
                "ST11001",
                "GROUP11001"
        );
    }

    private Course createCourse() {
        return new Course(
                11001,
                "Concurrency Test Course",
                "CONC11001",
                3
        );
    }

    @Test
    void testCalculateAverage() throws Exception {
        Student student = createStudent();
        Course course = createCourse();

        List<Grade> grades = List.of(
                new Grade(11001, student, course, 80),
                new Grade(11002, student, course, 90),
                new Grade(11003, student, course, 100)
        );

        GradeProcessingTask task = new GradeProcessingTask(grades);

        Double result = task.call();

        assertEquals(90.0, result);
    }

    @Test
    void testCalculateAverageWithOneGrade() throws Exception {
        Student student = createStudent();
        Course course = createCourse();

        List<Grade> grades = List.of(
                new Grade(11004, student, course, 85)
        );

        GradeProcessingTask task = new GradeProcessingTask(grades);

        assertEquals(85.0, task.call());
    }

    @Test
    void testCalculateAverageWithEmptyList() throws Exception {
        GradeProcessingTask task = new GradeProcessingTask(List.of());

        assertEquals(0.0, task.call());
    }

    @Test
    void testCalculateAverageWithDifferentGrades() throws Exception {
        Student student = createStudent();
        Course course = createCourse();

        List<Grade> grades = List.of(
                new Grade(11005, student, course, 50),
                new Grade(11006, student, course, 60),
                new Grade(11007, student, course, 70)
        );

        GradeProcessingTask task = new GradeProcessingTask(grades);

        assertEquals(60.0, task.call());
    }

    @Test
    void testTaskImplementsCallable() {
        Student student = createStudent();
        Course course = createCourse();

        List<Grade> grades = List.of(
                new Grade(11008, student, course, 95)
        );

        GradeProcessingTask task = new GradeProcessingTask(grades);

        assertInstanceOf(
                java.util.concurrent.Callable.class,
                task
        );
    }
}