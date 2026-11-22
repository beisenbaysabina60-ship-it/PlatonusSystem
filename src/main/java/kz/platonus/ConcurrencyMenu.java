package kz.platonus;

import kz.platonus.concurrency.GradeConcurrencyService;
import kz.platonus.dao.GradeDAO;
import kz.platonus.model.Grade;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Future;

public class ConcurrencyMenu {

    private final Scanner scanner;
    private final GradeDAO gradeDAO;

    public ConcurrencyMenu(Scanner scanner) {
        this.scanner = scanner;
        this.gradeDAO = new GradeDAO();
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n===== MULTITHREADING =====");
            System.out.println("1. Calculate average grade asynchronously");
            System.out.println("2. Calculate average with custom thread count");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    calculateAverage();
                    break;

                case "2":
                    calculateWithThreads();
                    break;

                case "0":
                    return;

                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    // Calculate average asynchronously
    private void calculateAverage() {
        List<Grade> grades = gradeDAO.findAll();

        if (grades.isEmpty()) {
            System.out.println("No grades available.");
            return;
        }

        GradeConcurrencyService service =
                new GradeConcurrencyService();

        try {
            System.out.println("Processing grades asynchronously...");

            Future<Double> result =
                    service.calculateAverageAsync(grades);

            double average = result.get();

            System.out.println("\n===== RESULT =====");
            System.out.println("Total grades: " + grades.size());
            System.out.printf("Average grade: %.2f%n", average);
            System.out.println("Task completed successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

        } finally {
            service.shutdown();
        }
    }

    // Calculate average using custom thread count
    private void calculateWithThreads() {
        try {
            System.out.print("Enter thread count: ");

            int threadCount =
                    Integer.parseInt(scanner.nextLine());

            if (threadCount <= 0) {
                System.out.println(
                        "Thread count must be greater than zero!");
                return;
            }

            List<Grade> grades = gradeDAO.findAll();

            if (grades.isEmpty()) {
                System.out.println("No grades available.");
                return;
            }

            System.out.println(
                    "Processing grades using " +
                    threadCount + " threads...");

            GradeConcurrencyService service =
                    new GradeConcurrencyService();

            try {
                Future<Double> result =
                        service.calculateAverageAsync(
                                grades, threadCount);

                double average = result.get();

                System.out.println("\n===== RESULT =====");
                System.out.println("Thread count: " + threadCount);
                System.out.println("Total grades: " + grades.size());
                System.out.printf(
                        "Average grade: %.2f%n", average);

                System.out.println(
                        "Multithreading task completed!");

            } finally {
                service.shutdown();
            }

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}