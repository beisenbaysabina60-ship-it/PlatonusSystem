package kz.platonus;

import kz.platonus.dao.LessonDAO;
import kz.platonus.dao.CourseDAO;
import kz.platonus.dao.TeacherDAO;
import kz.platonus.dao.StudentGroupDAO;

import kz.platonus.model.Lesson;
import kz.platonus.model.Course;
import kz.platonus.model.Teacher;
import kz.platonus.model.StudentGroup;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class LessonMenu {

    private final LessonDAO lessonDAO;
    private final CourseDAO courseDAO;
    private final TeacherDAO teacherDAO;
    private final StudentGroupDAO groupDAO;
    private final Scanner scanner;

    public LessonMenu(Scanner scanner) {
        this.lessonDAO = new LessonDAO();
        this.courseDAO = new CourseDAO();
        this.teacherDAO = new TeacherDAO();
        this.groupDAO = new StudentGroupDAO();
        this.scanner = scanner;
    }

    public void showMenu() {

        boolean running = true;

        while (running) {

            System.out.println("\n===== LESSON MANAGEMENT =====");
            System.out.println("1. Add lesson");
            System.out.println("2. View all lessons");
            System.out.println("3. Find lesson by ID");
            System.out.println("4. Update lesson");
            System.out.println("5. Delete lesson");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addLesson();
                    break;

                case "2":
                    viewAllLessons();
                    break;

                case "3":
                    findLesson();
                    break;

                case "4":
                    updateLesson();
                    break;

                case "5":
                    deleteLesson();
                    break;

                case "0":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    // CREATE
    private void addLesson() {

        try {
            System.out.print("Lesson ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Course ID: ");
            int courseId = Integer.parseInt(scanner.nextLine());

            Course course = courseDAO.findById(courseId);

            if (course == null) {
                System.out.println("Course not found!");
                return;
            }

            System.out.print("Teacher ID: ");
            int teacherId = Integer.parseInt(scanner.nextLine());

            Teacher teacher = teacherDAO.findById(teacherId);

            if (teacher == null) {
                System.out.println("Teacher not found!");
                return;
            }

            System.out.print("Group ID: ");
            int groupId = Integer.parseInt(scanner.nextLine());

            StudentGroup group = groupDAO.findById(groupId);

            if (group == null) {
                System.out.println("Group not found!");
                return;
            }

            System.out.print("Date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());

            System.out.print("Start time (HH:mm): ");
            LocalTime startTime = LocalTime.parse(scanner.nextLine());

            System.out.print("End time (HH:mm): ");
            LocalTime endTime = LocalTime.parse(scanner.nextLine());

            System.out.print("Room: ");
            String room = scanner.nextLine();

            Lesson lesson = new Lesson(
                    id,
                    course,
                    teacher,
                    group,
                    date,
                    startTime,
                    endTime,
                    room
            );

            lessonDAO.addLesson(lesson);

        } catch (Exception e) {
            System.out.println("Error adding lesson: " + e.getMessage());
        }
    }

    // READ ALL
    private void viewAllLessons() {

        List<Lesson> lessons = lessonDAO.findAll();

        if (lessons.isEmpty()) {
            System.out.println("No lessons found.");
            return;
        }

        System.out.println("\n===== ALL LESSONS =====");

        for (Lesson lesson : lessons) {
            printLesson(lesson);
        }
    }

    // FIND
    private void findLesson() {

        try {
            System.out.print("Enter lesson ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            Lesson lesson = lessonDAO.findById(id);

            if (lesson == null) {
                System.out.println("Lesson not found.");
            } else {
                printLesson(lesson);
            }

        } catch (Exception e) {
            System.out.println("Error finding lesson: " + e.getMessage());
        }
    }

    // UPDATE
    private void updateLesson() {

        try {
            System.out.print("Enter lesson ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            Lesson lesson = lessonDAO.findById(id);

            if (lesson == null) {
                System.out.println("Lesson not found.");
                return;
            }

            System.out.println("\nCurrent lesson information:");
            printLesson(lesson);

            System.out.print("New Course ID: ");
            int courseId = Integer.parseInt(scanner.nextLine());

            Course course = courseDAO.findById(courseId);

            if (course == null) {
                System.out.println("Course not found!");
                return;
            }

            System.out.print("New Teacher ID: ");
            int teacherId = Integer.parseInt(scanner.nextLine());

            Teacher teacher = teacherDAO.findById(teacherId);

            if (teacher == null) {
                System.out.println("Teacher not found!");
                return;
            }

            System.out.print("New Group ID: ");
            int groupId = Integer.parseInt(scanner.nextLine());

            StudentGroup group = groupDAO.findById(groupId);

            if (group == null) {
                System.out.println("Group not found!");
                return;
            }

            System.out.print("New date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());

            System.out.print("New start time (HH:mm): ");
            LocalTime startTime = LocalTime.parse(scanner.nextLine());

            System.out.print("New end time (HH:mm): ");
            LocalTime endTime = LocalTime.parse(scanner.nextLine());

            System.out.print("New room: ");
            String room = scanner.nextLine();

            Lesson updatedLesson = new Lesson(
                    id,
                    course,
                    teacher,
                    group,
                    date,
                    startTime,
                    endTime,
                    room
            );

            lessonDAO.updateLesson(updatedLesson);

            System.out.println("Lesson successfully updated!");

        } catch (Exception e) {
            System.out.println("Error updating lesson: " + e.getMessage());
        }
    }

    // DELETE
    private void deleteLesson() {

        try {
            System.out.print("Enter lesson ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            Lesson lesson = lessonDAO.findById(id);

            if (lesson == null) {
                System.out.println("Lesson not found.");
                return;
            }

            printLesson(lesson);

            System.out.print("Are you sure? (yes/no): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("yes")) {
                lessonDAO.deleteLesson(id);
                System.out.println("Lesson successfully deleted!");
            } else {
                System.out.println("Deletion cancelled.");
            }

        } catch (Exception e) {
            System.out.println("Error deleting lesson: " + e.getMessage());
        }
    }

    // PRINT
    private void printLesson(Lesson lesson) {

        System.out.println("----------------------------");
        System.out.println("ID: " + lesson.getId());
        System.out.println("Course: " + lesson.getCourse().getCourseName());
        System.out.println("Teacher: " + lesson.getTeacher().getFullName());
        System.out.println("Group: " + lesson.getGroup().getGroupName());
        System.out.println("Date: " + lesson.getDate());
        System.out.println("Start time: " + lesson.getStartTime());
        System.out.println("End time: " + lesson.getEndTime());
        System.out.println("Room: " + lesson.getRoom());
        System.out.println("----------------------------");
    }
}