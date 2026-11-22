package kz.platonus;

import kz.platonus.dao.TeacherDAO;
import kz.platonus.model.Teacher;

import java.util.List;
import java.util.Scanner;

public class TeacherMenu {

    private final TeacherDAO teacherDAO;
    private final Scanner scanner;

    public TeacherMenu(Scanner scanner) {
        this.teacherDAO = new TeacherDAO();
        this.scanner = scanner;
    }

    public void showMenu() {

        boolean running = true;

        while (running) {

            System.out.println("\n===== TEACHER MANAGEMENT =====");
            System.out.println("1. Add teacher");
            System.out.println("2. View all teachers");
            System.out.println("3. Find teacher by ID");
            System.out.println("4. Update teacher");
            System.out.println("5. Delete teacher");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    addTeacher();
                    break;

                case "2":
                    viewAllTeachers();
                    break;

                case "3":
                    findTeacher();
                    break;

                case "4":
                    updateTeacher();
                    break;

                case "5":
                    deleteTeacher();
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

    // ADD
    private void addTeacher() {

        try {
            System.out.print("Teacher ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Full name: ");
            String fullName = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Department: ");
            String department = scanner.nextLine();

            System.out.print("Academic degree: ");
            String academicDegree = scanner.nextLine();

            Teacher teacher = new Teacher(
                    id,
                    fullName,
                    email,
                    department,
                    academicDegree
            );

            teacherDAO.addTeacher(teacher);

        } catch (Exception e) {
            System.out.println("Error adding teacher: " + e.getMessage());
        }
    }

    // VIEW ALL
    private void viewAllTeachers() {

        List<Teacher> teachers = teacherDAO.findAll();

        if (teachers.isEmpty()) {
            System.out.println("No teachers found.");
            return;
        }

        System.out.println("\n===== ALL TEACHERS =====");

        for (Teacher teacher : teachers) {
            printTeacher(teacher);
        }
    }

    // FIND BY ID
    private void findTeacher() {

        try {
            System.out.print("Enter teacher ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            Teacher teacher = teacherDAO.findById(id);

            if (teacher == null) {
                System.out.println("Teacher not found.");
            } else {
                printTeacher(teacher);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // UPDATE
    private void updateTeacher() {

        try {
            System.out.print("Enter teacher ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            Teacher teacher = teacherDAO.findById(id);

            if (teacher == null) {
                System.out.println("Teacher not found.");
                return;
            }

            System.out.println("\nCurrent teacher information:");
            printTeacher(teacher);

            System.out.print("New full name: ");
            teacher.setFullName(scanner.nextLine());

            System.out.print("New email: ");
            teacher.setEmail(scanner.nextLine());

            System.out.print("New department: ");
            teacher.setDepartment(scanner.nextLine());

            System.out.print("New academic degree: ");
            teacher.setAcademicDegree(scanner.nextLine());

            teacherDAO.updateTeacher(teacher);

            System.out.println("Teacher successfully updated!");

        } catch (Exception e) {
            System.out.println("Error updating teacher: " + e.getMessage());
        }
    }

    // DELETE
    private void deleteTeacher() {

        try {
            System.out.print("Enter teacher ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            Teacher teacher = teacherDAO.findById(id);

            if (teacher == null) {
                System.out.println("Teacher not found.");
                return;
            }

            printTeacher(teacher);

            System.out.print("Are you sure? (yes/no): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("yes")) {
                teacherDAO.deleteTeacher(id);
                System.out.println("Teacher deleted!");
            } else {
                System.out.println("Deletion cancelled.");
            }

        } catch (Exception e) {
            System.out.println("Error deleting teacher: " + e.getMessage());
        }
    }

    // PRINT
    private void printTeacher(Teacher teacher) {

        System.out.println("----------------------------");
        System.out.println("ID: " + teacher.getId());
        System.out.println("Name: " + teacher.getFullName());
        System.out.println("Email: " + teacher.getEmail());
        System.out.println("Department: " + teacher.getDepartment());
        System.out.println("Academic Degree: " + teacher.getAcademicDegree());
        System.out.println("----------------------------");
    }
}