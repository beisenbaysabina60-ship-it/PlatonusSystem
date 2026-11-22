package kz.platonus;

import kz.platonus.dao.GradeDAO;
import kz.platonus.model.Grade;

import java.util.List;
import java.util.Scanner;

public class GradeStatisticsMenu {

    private final Scanner scanner;
    private final GradeDAO gradeDAO;

    public GradeStatisticsMenu(Scanner scanner) {
        this.scanner = scanner;
        this.gradeDAO = new GradeDAO();
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n===== GRADE STATISTICS =====");
            System.out.println("1. Overall statistics");
            System.out.println("2. Average grade by student");
            System.out.println("3. Average grade by course");
            System.out.println("4. Highest grade");
            System.out.println("5. Lowest grade");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    overallStatistics();
                    break;
                case "2":
                    averageByStudent();
                    break;
                case "3":
                    averageByCourse();
                    break;
                case "4":
                    highestGrade();
                    break;
                case "5":
                    lowestGrade();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void overallStatistics() {
        List<Grade> grades = gradeDAO.findAll();

        if (grades.isEmpty()) {
            System.out.println("No grades available.");
            return;
        }

        double total = 0;

        for (Grade grade : grades) {
            total += grade.getScore();
        }

        double average = total / grades.size();

        System.out.println("\n===== OVERALL STATISTICS =====");
        System.out.println("Total grades: " + grades.size());
        System.out.printf("Average score: %.2f%n", average);

        int a = 0, b = 0, c = 0, d = 0, f = 0;

        for (Grade grade : grades) {
            String letter = grade.getLetterGrade();

            if (letter.startsWith("A")) {
                a++;
            } else if (letter.startsWith("B")) {
                b++;
            } else if (letter.startsWith("C")) {
                c++;
            } else if (letter.equals("D")) {
                d++;
            } else {
                f++;
            }
        }

        System.out.println("\nGrade distribution:");
        System.out.println("A: " + a);
        System.out.println("B: " + b);
        System.out.println("C: " + c);
        System.out.println("D: " + d);
        System.out.println("F: " + f);
    }

    private void averageByStudent() {
        try {
            System.out.print("Enter student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());

            List<Grade> grades = gradeDAO.findByStudentId(studentId);

            if (grades.isEmpty()) {
                System.out.println("No grades found for this student.");
                return;
            }

            double total = 0;

            for (Grade grade : grades) {
                total += grade.getScore();
            }

            double average = total / grades.size();

            System.out.println("\nStudent: " +
                    grades.get(0).getStudent().getFullName());

            System.out.println("Number of grades: " + grades.size());
            System.out.printf("Average score: %.2f%n", average);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void averageByCourse() {
        try {
            System.out.print("Enter course ID: ");
            int courseId = Integer.parseInt(scanner.nextLine());

            List<Grade> grades = gradeDAO.findByCourseId(courseId);

            if (grades.isEmpty()) {
                System.out.println("No grades found for this course.");
                return;
            }

            double total = 0;

            for (Grade grade : grades) {
                total += grade.getScore();
            }

            double average = total / grades.size();

            System.out.println("\nCourse: " +
                    grades.get(0).getCourse().getCourseName());

            System.out.println("Number of grades: " + grades.size());
            System.out.printf("Average score: %.2f%n", average);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void highestGrade() {
        List<Grade> grades = gradeDAO.findAll();

        if (grades.isEmpty()) {
            System.out.println("No grades available.");
            return;
        }

        Grade highest = grades.get(0);

        for (Grade grade : grades) {
            if (grade.getScore() > highest.getScore()) {
                highest = grade;
            }
        }

        System.out.println("\n===== HIGHEST GRADE =====");
        printGrade(highest);
    }

    private void lowestGrade() {
        List<Grade> grades = gradeDAO.findAll();

        if (grades.isEmpty()) {
            System.out.println("No grades available.");
            return;
        }

        Grade lowest = grades.get(0);

        for (Grade grade : grades) {
            if (grade.getScore() < lowest.getScore()) {
                lowest = grade;
            }
        }

        System.out.println("\n===== LOWEST GRADE =====");
        printGrade(lowest);
    }

    private void printGrade(Grade grade) {
        System.out.println("----------------------------");
        System.out.println("Grade ID: " + grade.getId());
        System.out.println("Student: " + grade.getStudent().getFullName());
        System.out.println("Course: " + grade.getCourse().getCourseName());
        System.out.println("Score: " + grade.getScore());
        System.out.println("Letter grade: " + grade.getLetterGrade());
    }
}