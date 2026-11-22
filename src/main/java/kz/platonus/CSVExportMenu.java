package kz.platonus;

import kz.platonus.export.GradeCSVExporter;

import java.util.Scanner;

public class CSVExportMenu {

    private final Scanner scanner;
    private final GradeCSVExporter exporter;

    public CSVExportMenu(Scanner scanner) {
        this.scanner = scanner;
        this.exporter = new GradeCSVExporter();
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n===== CSV EXPORT =====");
            System.out.println("1. Export all grades");
            System.out.println("2. Export grades by student");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    exportAll();
                    break;
                case "2":
                    exportStudent();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void exportAll() {
        System.out.print("Enter file name (example: grades.csv): ");
        String filePath = scanner.nextLine();

        if (filePath.isBlank()) {
            filePath = "grades.csv";
        }

        try {
            exporter.exportAllGrades(filePath);
        } catch (Exception e) {
            System.out.println("Export failed: " + e.getMessage());
        }
    }

    private void exportStudent() {
        try {
            System.out.print("Enter student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter file name (example: student_grades.csv): ");
            String filePath = scanner.nextLine();

            if (filePath.isBlank()) {
                filePath = "student_grades.csv";
            }

            exporter.exportStudentGrades(studentId, filePath);

        } catch (Exception e) {
            System.out.println("Export failed: " + e.getMessage());
        }
    }
}