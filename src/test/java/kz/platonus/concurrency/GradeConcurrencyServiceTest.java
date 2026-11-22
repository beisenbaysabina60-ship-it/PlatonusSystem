package kz.platonus.concurrency;

import kz.platonus.model.Course;
import kz.platonus.model.Grade;
import kz.platonus.model.Student;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

class GradeConcurrencyServiceTest {

    private Student createStudent() {
        return new Student(
                12001,
                "Concurrency Service Student",
                "serviceconcurrency@example.com",
                "ST12001",
                "GROUP12001"
        );
    }

    private Course createCourse() {
        return new Course(
                12001,
                "Concurrency Service Course",
                "CONCSERVICE12001",
                3
        );
    }

    @Test
    void testCalculateAverageAsync() throws Exception {
        Student student = createStudent();
        Course course = createCourse();

        List<Grade> grades = List.of(
                new Grade(12001, student, course, 80),
                new Grade(12002, student, course, 90),
                new Grade(12003, student, course, 100)
        );

        GradeConcurrencyService service = new GradeConcurrencyService();

        try {
            Future<Double> future = service.calculateAverageAsync(grades);

            assertEquals(90.0, future.get());
        } finally {
            service.shutdown();
        }
    }

    @Test
    void testCalculateAverageWithEmptyList() throws Exception {
        GradeConcurrencyService service = new GradeConcurrencyService();

        try {
            Future<Double> future = service.calculateAverageAsync(List.of());

            assertEquals(0.0, future.get());
        } finally {
            service.shutdown();
        }
    }

    @Test
    void testCalculateAverageWithOneGrade() throws Exception {
        Student student = createStudent();
        Course course = createCourse();

        List<Grade> grades = List.of(
                new Grade(12004, student, course, 75)
        );

        GradeConcurrencyService service = new GradeConcurrencyService();

        try {
            Future<Double> future = service.calculateAverageAsync(grades);

            assertEquals(75.0, future.get());
        } finally {
            service.shutdown();
        }
    }

    @Test
    void testFutureReturnsResult() throws Exception {
        Student student = createStudent();
        Course course = createCourse();

        List<Grade> grades = List.of(
                new Grade(12005, student, course, 95)
        );

        GradeConcurrencyService service = new GradeConcurrencyService();

        try {
            Future<Double> future = service.calculateAverageAsync(grades);

            assertTrue(future.isDone() || future.get() == 95.0);
            assertEquals(95.0, future.get());
        } finally {
            service.shutdown();
        }
    }

    @Test
    void testCalculateAverageWithMultipleGrades() throws Exception {
        Student student = createStudent();
        Course course = createCourse();

        List<Grade> grades = List.of(
                new Grade(12006, student, course, 60),
                new Grade(12007, student, course, 70),
                new Grade(12008, student, course, 80),
                new Grade(12009, student, course, 90)
        );

        GradeConcurrencyService service = new GradeConcurrencyService();

        try {
            Future<Double> future = service.calculateAverageAsync(grades);

            assertEquals(75.0, future.get());
        } finally {
            service.shutdown();
        }
    }
}