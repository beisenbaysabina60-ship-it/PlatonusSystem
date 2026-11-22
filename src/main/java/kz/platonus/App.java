package kz.platonus;

import kz.platonus.database.DatabaseInitializer;

import java.util.Scanner;

public class App {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            DatabaseInitializer.initialize();
            System.out.println("Database initialized successfully!");
        } catch (Exception e) {
            System.out.println("Database initialization error: " + e.getMessage());
            return;
        }

        boolean running = true;

        while (running) {

            printMenu();

            System.out.print("Choose option: ");
            String choice = scanner.nextLine();

            switch (choice) {

               case "1":
    StudentMenu studentMenu = new StudentMenu(scanner);
    studentMenu.showMenu();
    break;

                case "2": {
    TeacherMenu teacherMenu = new TeacherMenu(scanner);
    teacherMenu.showMenu();
    break;
}

                case "3": {
    CourseMenu courseMenu = new CourseMenu(scanner);
    courseMenu.showMenu();
    break;
}

                case "4": {
    StudentGroupMenu groupMenu = new StudentGroupMenu(scanner);
    groupMenu.showMenu();
    break;
}

                case "5": {
    LessonMenu lessonMenu = new LessonMenu(scanner);
    lessonMenu.showMenu();
    break;
}

               case "6": {
    AttendanceMenu attendanceMenu = new AttendanceMenu(scanner);
    attendanceMenu.showMenu();
    break;
}

                case "7": {
    GradeMenu gradeMenu = new GradeMenu(scanner);
    gradeMenu.showMenu();
    break;
}

                case "8": {
    GradeStatisticsMenu statisticsMenu =
            new GradeStatisticsMenu(scanner);
    statisticsMenu.showMenu();
    break;
}
case "9": {
    CSVExportMenu csvMenu = new CSVExportMenu(scanner);
    csvMenu.showMenu();
    break;
}
                    

                case "10": {
    JSONExportMenu jsonMenu = new JSONExportMenu(scanner);
    jsonMenu.showMenu();
    break;
}
case "11": {
    ConcurrencyMenu concurrencyMenu =
            new ConcurrencyMenu(scanner);
    concurrencyMenu.showMenu();
    break;
}

                case "0":
                    running = false;
                    System.out.println("Exiting Platonus System...");
                    break;

                default:
                    System.out.println("Invalid option! Please try again.");
            }

            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private static void printMenu() {

        System.out.println("\n==================================");
        System.out.println("       PLATONUS SYSTEM");
        System.out.println("==================================");
        System.out.println("1.  Students");
        System.out.println("2.  Teachers");
        System.out.println("3.  Courses");
        System.out.println("4.  Student Groups");
        System.out.println("5.  Lessons");
        System.out.println("6.  Attendance");
        System.out.println("7.  Grades");
        System.out.println("8.  Grade Statistics");
        System.out.println("9.  Export Grades to CSV");
        System.out.println("10. Export Grades to JSON");
        System.out.println("11. Multithreading");
        System.out.println("0.  Exit");
        System.out.println("==================================");
    }
}