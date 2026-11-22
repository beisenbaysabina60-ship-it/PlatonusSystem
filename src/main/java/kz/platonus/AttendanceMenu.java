package kz.platonus;

import kz.platonus.dao.AttendanceDAO;
import kz.platonus.dao.StudentDAO;
import kz.platonus.dao.LessonDAO;

import kz.platonus.model.Attendance;
import kz.platonus.model.AttendanceStatus;
import kz.platonus.model.Student;
import kz.platonus.model.Lesson;

import java.util.List;
import java.util.Scanner;

public class AttendanceMenu {

    private final AttendanceDAO attendanceDAO;
    private final StudentDAO studentDAO;
    private final LessonDAO lessonDAO;
    private final Scanner scanner;

    public AttendanceMenu(Scanner scanner) {
        this.attendanceDAO = new AttendanceDAO();
        this.studentDAO = new StudentDAO();
        this.lessonDAO = new LessonDAO();
        this.scanner = scanner;
    }

    public void showMenu() {

        boolean running = true;

        while (running) {

            System.out.println("\n===== ATTENDANCE MANAGEMENT =====");
            System.out.println("1. Record attendance");
            System.out.println("2. View all attendance");
            System.out.println("3. Find attendance by ID");
            System.out.println("4. Update attendance");
            System.out.println("5. Delete attendance");
            System.out.println("6. View attendance by student");
            System.out.println("7. View attendance by lesson");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    recordAttendance();
                    break;

                case "2":
                    viewAllAttendance();
                    break;

                case "3":
                    findAttendance();
                    break;

                case "4":
                    updateAttendance();
                    break;

                case "5":
                    deleteAttendance();
                    break;

                case "6":
                    viewByStudent();
                    break;

                case "7":
                    viewByLesson();
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
    private void recordAttendance() {

        try {
            System.out.print("Attendance ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());

            Student student = studentDAO.findById(studentId);

            if (student == null) {
                System.out.println("Student not found!");
                return;
            }

            System.out.print("Lesson ID: ");
            int lessonId = Integer.parseInt(scanner.nextLine());

            Lesson lesson = lessonDAO.findById(lessonId);

            if (lesson == null) {
                System.out.println("Lesson not found!");
                return;
            }

            System.out.println("Statuses: PRESENT, ABSENT, LATE, EXCUSED");
            System.out.print("Status: ");

            AttendanceStatus status =
                    AttendanceStatus.valueOf(scanner.nextLine().toUpperCase());

            System.out.print("Comment: ");
            String comment = scanner.nextLine();

            Attendance attendance = new Attendance(
                    id,
                    student,
                    lesson,
                    status,
                    comment
            );

            attendanceDAO.addAttendance(attendance);

        } catch (Exception e) {
            System.out.println("Error recording attendance: " + e.getMessage());
        }
    }

    // READ ALL
    private void viewAllAttendance() {

        List<Attendance> attendances = attendanceDAO.findAll();

        if (attendances.isEmpty()) {
            System.out.println("No attendance records found.");
            return;
        }

        System.out.println("\n===== ALL ATTENDANCE RECORDS =====");

        for (Attendance attendance : attendances) {
            printAttendance(attendance);
        }
    }

    // FIND BY ID
    private void findAttendance() {

        try {
            System.out.print("Enter attendance ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            Attendance attendance = attendanceDAO.findById(id);

            if (attendance == null) {
                System.out.println("Attendance not found.");
            } else {
                printAttendance(attendance);
            }

        } catch (Exception e) {
            System.out.println("Error finding attendance: " + e.getMessage());
        }
    }

    // UPDATE
    private void updateAttendance() {

        try {
            System.out.print("Enter attendance ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            Attendance attendance = attendanceDAO.findById(id);

            if (attendance == null) {
                System.out.println("Attendance not found.");
                return;
            }

            System.out.println("\nCurrent attendance:");
            printAttendance(attendance);

            System.out.println("Statuses: PRESENT, ABSENT, LATE, EXCUSED");
            System.out.print("New status: ");

            AttendanceStatus status =
                    AttendanceStatus.valueOf(scanner.nextLine().toUpperCase());

            System.out.print("New comment: ");
            String comment = scanner.nextLine();

            attendance.setStatus(status);
            attendance.setComment(comment);
            attendance.updateRecordedAt();

            attendanceDAO.updateAttendance(attendance);

        } catch (Exception e) {
            System.out.println("Error updating attendance: " + e.getMessage());
        }
    }

    // DELETE
    private void deleteAttendance() {

        try {
            System.out.print("Enter attendance ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            Attendance attendance = attendanceDAO.findById(id);

            if (attendance == null) {
                System.out.println("Attendance not found.");
                return;
            }

            printAttendance(attendance);

            System.out.print("Are you sure? (yes/no): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("yes")) {
                attendanceDAO.deleteAttendance(id);
            } else {
                System.out.println("Deletion cancelled.");
            }

        } catch (Exception e) {
            System.out.println("Error deleting attendance: " + e.getMessage());
        }
    }

    // BY STUDENT
    private void viewByStudent() {

        try {
            System.out.print("Enter student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());

            Student student = studentDAO.findById(studentId);

            if (student == null) {
                System.out.println("Student not found.");
                return;
            }

            List<Attendance> attendances =
                    attendanceDAO.findByStudentId(studentId);

            if (attendances.isEmpty()) {
                System.out.println("No attendance records for this student.");
                return;
            }

            System.out.println("\nAttendance for: " + student.getFullName());

            for (Attendance attendance : attendances) {
                printAttendance(attendance);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // BY LESSON
    private void viewByLesson() {

        try {
            System.out.print("Enter lesson ID: ");
            int lessonId = Integer.parseInt(scanner.nextLine());

            Lesson lesson = lessonDAO.findById(lessonId);

            if (lesson == null) {
                System.out.println("Lesson not found.");
                return;
            }

            List<Attendance> attendances =
                    attendanceDAO.findByLessonId(lessonId);

            if (attendances.isEmpty()) {
                System.out.println("No attendance records for this lesson.");
                return;
            }

            System.out.println("\nAttendance for lesson: "
                    + lesson.getCourse().getCourseName());

            for (Attendance attendance : attendances) {
                printAttendance(attendance);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // PRINT
    private void printAttendance(Attendance attendance) {

        System.out.println("----------------------------");
        System.out.println("Attendance ID: " + attendance.getId());
        System.out.println("Student: " + attendance.getStudent().getFullName());
        System.out.println("Lesson: " + attendance.getLesson().getCourse().getCourseName());
        System.out.println("Date: " + attendance.getLesson().getDate());
        System.out.println("Status: " + attendance.getStatus());
        System.out.println("Recorded at: " + attendance.getRecordedAt());
        System.out.println("Comment: " + attendance.getComment());
        System.out.println("----------------------------");
    }
}