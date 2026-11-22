package kz.platonus;

import kz.platonus.dao.CourseDAO;
import kz.platonus.model.Course;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CourseMenu {

    private final CourseDAO courseDAO;
    private final Scanner scanner;

    public CourseMenu(Scanner scanner) {
        this.courseDAO = new CourseDAO();
        this.scanner = scanner;
    }

    public void showMenu() {

        boolean running = true;

        while (running) {

            System.out.println("\n===== COURSE MANAGEMENT =====");
            System.out.println("1. Add course");
            System.out.println("2. View all courses");
            System.out.println("3. Find course by ID");
            System.out.println("4. Update course");
            System.out.println("5. Delete course");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addCourse();
                    break;

                case "2":
                    viewAllCourses();
                    break;

                case "3":
                    findCourse();
                    break;

                case "4":
                    updateCourse();
                    break;

                case "5":
                    deleteCourse();
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
    private void addCourse() {

        try {
            System.out.print("Course ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Course name: ");
            String name = scanner.nextLine();

            System.out.print("Course code: ");
            String code = scanner.nextLine();

            System.out.print("Credits: ");
            int credits = Integer.parseInt(scanner.nextLine());

            Course course = new Course(id, name, code, credits);

            courseDAO.addCourse(course);

            System.out.println("Course successfully added!");

        } catch (Exception e) {
            System.out.println("Error adding course: " + e.getMessage());
        }
    }

    // READ ALL
    private void viewAllCourses() {

        try {
            List<Course> courses = courseDAO.findAll();

            if (courses.isEmpty()) {
                System.out.println("No courses found.");
                return;
            }

            System.out.println("\n===== ALL COURSES =====");

            for (Course course : courses) {
                printCourse(course);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving courses: " + e.getMessage());
        }
    }

    // FIND
    private void findCourse() {

        try {
            System.out.print("Enter course ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            Course course = courseDAO.findById(id);

            if (course == null) {
                System.out.println("Course not found.");
            } else {
                printCourse(course);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // UPDATE
    private void updateCourse() {

        try {
            System.out.print("Enter course ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            Course course = courseDAO.findById(id);

            if (course == null) {
                System.out.println("Course not found.");
                return;
            }

            System.out.println("\nCurrent course information:");
            printCourse(course);

            System.out.print("New course name: ");
            course.setCourseName(scanner.nextLine());

            System.out.print("New course code: ");
            course.setCourseCode(scanner.nextLine());

            System.out.print("New credits: ");
            course.setCredits(Integer.parseInt(scanner.nextLine()));

            boolean updated = courseDAO.updateCourse(course);

            if (updated) {
                System.out.println("Course successfully updated!");
            } else {
                System.out.println("Course update failed.");
            }

        } catch (Exception e) {
            System.out.println("Error updating course: " + e.getMessage());
        }
    }

    // DELETE
    private void deleteCourse() {

        try {
            System.out.print("Enter course ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            Course course = courseDAO.findById(id);

            if (course == null) {
                System.out.println("Course not found.");
                return;
            }

            printCourse(course);

            System.out.print("Are you sure? (yes/no): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("yes")) {

                boolean deleted = courseDAO.deleteCourse(id);

                if (deleted) {
                    System.out.println("Course successfully deleted!");
                } else {
                    System.out.println("Course deletion failed.");
                }

            } else {
                System.out.println("Deletion cancelled.");
            }

        } catch (Exception e) {
            System.out.println("Error deleting course: " + e.getMessage());
        }
    }

    // PRINT
    private void printCourse(Course course) {

        System.out.println("----------------------------");
        System.out.println("ID: " + course.getId());
        System.out.println("Name: " + course.getCourseName());
        System.out.println("Code: " + course.getCourseCode());
        System.out.println("Credits: " + course.getCredits());
        System.out.println("----------------------------");
    }
}