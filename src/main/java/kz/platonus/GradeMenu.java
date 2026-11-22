package kz.platonus;

import kz.platonus.dao.GradeDAO;
import kz.platonus.dao.StudentDAO;
import kz.platonus.dao.CourseDAO;
import kz.platonus.model.Grade;
import kz.platonus.model.Student;
import kz.platonus.model.Course;

import java.util.List;
import java.util.Scanner;

public class GradeMenu {

    private final Scanner scanner;
    private final GradeDAO gradeDAO;
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;

    public GradeMenu(Scanner scanner) {
        this.scanner = scanner;
        this.gradeDAO = new GradeDAO();
        this.studentDAO = new StudentDAO();
        this.courseDAO = new CourseDAO();
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n===== GRADE MANAGEMENT =====");
            System.out.println("1. Add grade");
            System.out.println("2. View all grades");
            System.out.println("3. Find grade by ID");
            System.out.println("4. Update grade");
            System.out.println("5. Delete grade");
            System.out.println("6. View grades by student");
            System.out.println("7. View grades by course");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addGrade();
                    break;
                case "2":
                    viewAllGrades();
                    break;
                case "3":
                    findGradeById();
                    break;
                case "4":
                    updateGrade();
                    break;
                case "5":
                    deleteGrade();
                    break;
                case "6":
                    viewGradesByStudent();
                    break;
                case "7":
                    viewGradesByCourse();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    // ADD GRADE
    private void addGrade() {
        try {
            System.out.print("Grade ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());

            Student student = studentDAO.findById(studentId);

            if (student == null) {
                System.out.println("Student not found!");
                return;
            }

            System.out.print("Course ID: ");
            int courseId = Integer.parseInt(scanner.nextLine());

            Course course = courseDAO.findById(courseId);

            if (course == null) {
                System.out.println("Course not found!");
                return;
            }

            System.out.print("Score (0-100): ");
            double score = Double.parseDouble(scanner.nextLine());

            Grade grade = new Grade(id, student, course, score);

            gradeDAO.addGrade(grade);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // VIEW ALL
    private void viewAllGrades() {
        List<Grade> grades = gradeDAO.findAll();

        if (grades.isEmpty()) {
            System.out.println("No grades found.");
            return;
        }

        System.out.println("\n===== ALL GRADES =====");

        for (Grade grade : grades) {
            printGrade(grade);
        }
    }

    // FIND BY ID
    private void findGradeById() {
        try {
            System.out.print("Enter grade ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            Grade grade = gradeDAO.findById(id);

            if (grade == null) {
                System.out.println("Grade not found!");
            } else {
                printGrade(grade);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // UPDATE
    private void updateGrade() {
        try {
            System.out.print("Enter grade ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            Grade grade = gradeDAO.findById(id);

            if (grade == null) {
                System.out.println("Grade not found!");
                return;
            }

            System.out.println("Current score: " + grade.getScore());
            System.out.println("Current letter grade: " + grade.getLetterGrade());

            System.out.print("Enter new score (0-100): ");
            double score = Double.parseDouble(scanner.nextLine());

            grade.setScore(score);

            gradeDAO.updateGrade(grade);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // DELETE
    private void deleteGrade() {
        try {
            System.out.print("Enter grade ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            Grade grade = gradeDAO.findById(id);

            if (grade == null) {
                System.out.println("Grade not found!");
                return;
            }

            System.out.print("Are you sure you want to delete? (yes/no): ");
            String answer = scanner.nextLine();

            if (answer.equalsIgnoreCase("yes")) {
                gradeDAO.deleteGrade(id);
            } else {
                System.out.println("Deletion cancelled.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // BY STUDENT
    private void viewGradesByStudent() {
        try {
            System.out.print("Enter student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());

            List<Grade> grades = gradeDAO.findByStudentId(studentId);

            if (grades.isEmpty()) {
                System.out.println("No grades found for this student.");
                return;
            }

            System.out.println("\n===== STUDENT GRADES =====");

            for (Grade grade : grades) {
                printGrade(grade);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // BY COURSE
    private void viewGradesByCourse() {
        try {
            System.out.print("Enter course ID: ");
            int courseId = Integer.parseInt(scanner.nextLine());

            List<Grade> grades = gradeDAO.findByCourseId(courseId);

            if (grades.isEmpty()) {
                System.out.println("No grades found for this course.");
                return;
            }

            System.out.println("\n===== COURSE GRADES =====");

            for (Grade grade : grades) {
                printGrade(grade);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // PRINT
    private void printGrade(Grade grade) {
        System.out.println("----------------------------");
        System.out.println("Grade ID: " + grade.getId());
        System.out.println("Student: " + grade.getStudent().getFullName());
        System.out.println("Course: " + grade.getCourse().getCourseName());
        System.out.println("Score: " + grade.getScore());
        System.out.println("Letter Grade: " + grade.getLetterGrade());
    }
}