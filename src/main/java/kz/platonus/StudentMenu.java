package kz.platonus;

import kz.platonus.dao.StudentDAO;
import kz.platonus.model.Student;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class StudentMenu {

    private final StudentDAO studentDAO;
    private final Scanner scanner;

    public StudentMenu(Scanner scanner) {
        this.studentDAO = new StudentDAO();
        this.scanner = scanner;
    }

    public void showMenu() {

        boolean running = true;

        while (running) {

            System.out.println("\n===== STUDENT MANAGEMENT =====");
            System.out.println("1. Add student");
            System.out.println("2. View all students");
            System.out.println("3. Find student by ID");
            System.out.println("4. Update student");
            System.out.println("5. Delete student");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addStudent();
                    break;

                case "2":
                    viewAllStudents();
                    break;

                case "3":
                    findStudent();
                    break;

                case "4":
                    updateStudent();
                    break;

                case "5":
                    deleteStudent();
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

    private void addStudent() {

        try {
            System.out.print("Student ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Full name: ");
            String name = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Student number: ");
            String studentNumber = scanner.nextLine();

            System.out.print("Group: ");
            String groupName = scanner.nextLine();

            Student student = new Student(
                    id,
                    name,
                    email,
                    studentNumber,
                    groupName
            );

            studentDAO.addStudent(student);

            System.out.println("Student successfully added!");

        } catch (Exception e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    private void viewAllStudents() {

        try {
            List<Student> students = studentDAO.findAll();

            if (students.isEmpty()) {
                System.out.println("No students found.");
                return;
            }

            System.out.println("\n===== ALL STUDENTS =====");

            for (Student student : students) {
                printStudent(student);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void findStudent() {

        try {
            System.out.print("Enter student ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            Student student = studentDAO.findById(id);

            if (student == null) {
                System.out.println("Student not found.");
            } else {
                printStudent(student);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateStudent() {

        try {
            System.out.print("Enter student ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            Student student = studentDAO.findById(id);

            if (student == null) {
                System.out.println("Student not found.");
                return;
            }

            System.out.println("Current student information:");
            printStudent(student);

            System.out.print("New full name: ");
            student.setFullName(scanner.nextLine());

            System.out.print("New email: ");
            student.setEmail(scanner.nextLine());

            System.out.print("New student number: ");
            student.setStudentNumber(scanner.nextLine());

            System.out.print("New group: ");
            student.setGroupName(scanner.nextLine());

            studentDAO.updateStudent(student);

            System.out.println("Student successfully updated!");

        } catch (Exception e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }

    private void deleteStudent() {

        try {
            System.out.print("Enter student ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            Student student = studentDAO.findById(id);

            if (student == null) {
                System.out.println("Student not found.");
                return;
            }

            printStudent(student);

            System.out.print("Are you sure? (yes/no): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("yes")) {
                studentDAO.deleteStudent(id);
                System.out.println("Student successfully deleted!");
            } else {
                System.out.println("Deletion cancelled.");
            }

        } catch (Exception e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }

    private void printStudent(Student student) {

        System.out.println("----------------------------");
        System.out.println("ID: " + student.getId());
        System.out.println("Name: " + student.getFullName());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Student Number: " + student.getStudentNumber());
        System.out.println("Group: " + student.getGroupName());
        System.out.println("----------------------------");
    }
}