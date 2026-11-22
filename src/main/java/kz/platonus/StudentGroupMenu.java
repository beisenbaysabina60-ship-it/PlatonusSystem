package kz.platonus;

import kz.platonus.dao.StudentGroupDAO;
import kz.platonus.model.StudentGroup;

import java.util.List;
import java.util.Scanner;

public class StudentGroupMenu {

    private final StudentGroupDAO groupDAO;
    private final Scanner scanner;

    public StudentGroupMenu(Scanner scanner) {
        this.groupDAO = new StudentGroupDAO();
        this.scanner = scanner;
    }

    public void showMenu() {

        boolean running = true;

        while (running) {

            System.out.println("\n===== STUDENT GROUP MANAGEMENT =====");
            System.out.println("1. Add group");
            System.out.println("2. View all groups");
            System.out.println("3. Find group by ID");
            System.out.println("4. Update group");
            System.out.println("5. Delete group");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addGroup();
                    break;

                case "2":
                    viewAllGroups();
                    break;

                case "3":
                    findGroup();
                    break;

                case "4":
                    updateGroup();
                    break;

                case "5":
                    deleteGroup();
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
    private void addGroup() {

        try {
            System.out.print("Group ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Group name: ");
            String groupName = scanner.nextLine();

            System.out.print("Specialty: ");
            String specialty = scanner.nextLine();

            StudentGroup group =
                    new StudentGroup(id, groupName, specialty);

            groupDAO.addGroup(group);

        } catch (Exception e) {
            System.out.println("Error adding group: " + e.getMessage());
        }
    }

    // READ ALL
    private void viewAllGroups() {

        List<StudentGroup> groups = groupDAO.findAll();

        if (groups.isEmpty()) {
            System.out.println("No groups found.");
            return;
        }

        System.out.println("\n===== ALL STUDENT GROUPS =====");

        for (StudentGroup group : groups) {
            printGroup(group);
        }
    }

    // FIND BY ID
    private void findGroup() {

        try {
            System.out.print("Enter group ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            StudentGroup group = groupDAO.findById(id);

            if (group == null) {
                System.out.println("Group not found.");
            } else {
                printGroup(group);
            }

        } catch (Exception e) {
            System.out.println("Error finding group: " + e.getMessage());
        }
    }

    // UPDATE
    private void updateGroup() {

        try {
            System.out.print("Enter group ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            StudentGroup group = groupDAO.findById(id);

            if (group == null) {
                System.out.println("Group not found.");
                return;
            }

            System.out.println("\nCurrent group information:");
            printGroup(group);

            System.out.print("New group name: ");
            group.setGroupName(scanner.nextLine());

            System.out.print("New specialty: ");
            group.setSpecialty(scanner.nextLine());

            groupDAO.updateGroup(group);

        } catch (Exception e) {
            System.out.println("Error updating group: " + e.getMessage());
        }
    }

    // DELETE
    private void deleteGroup() {

        try {
            System.out.print("Enter group ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            StudentGroup group = groupDAO.findById(id);

            if (group == null) {
                System.out.println("Group not found.");
                return;
            }

            printGroup(group);

            System.out.print("Are you sure? (yes/no): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("yes")) {
                groupDAO.deleteGroup(id);
            } else {
                System.out.println("Deletion cancelled.");
            }

        } catch (Exception e) {
            System.out.println("Error deleting group: " + e.getMessage());
        }
    }

    // PRINT
    private void printGroup(StudentGroup group) {

        System.out.println("----------------------------");
        System.out.println("ID: " + group.getId());
        System.out.println("Group name: " + group.getGroupName());
        System.out.println("Specialty: " + group.getSpecialty());
        System.out.println("----------------------------");
    }
}