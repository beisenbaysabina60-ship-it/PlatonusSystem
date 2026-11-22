package kz.platonus.model;

/**
 * Represents a student in the Platonus system.
 * Demonstrates inheritance and polymorphism.
 */
public class Student extends User {

    private String studentNumber;
    private String groupName;

    public Student(int id, String fullName, String email,
                   String studentNumber, String groupName) {

        super(id, fullName, email);

        if (studentNumber == null || studentNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Student number cannot be empty");
        }

        if (groupName == null || groupName.isBlank()) {
            throw new IllegalArgumentException(
                    "Group name cannot be empty");
        }

        this.studentNumber = studentNumber;
        this.groupName = groupName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setStudentNumber(String studentNumber) {
        if (studentNumber == null || studentNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Student number cannot be empty");
        }
        this.studentNumber = studentNumber;
    }

    public void setGroupName(String groupName) {
        if (groupName == null || groupName.isBlank()) {
            throw new IllegalArgumentException(
                    "Group name cannot be empty");
        }
        this.groupName = groupName;
    }

    @Override
    public UserRole getRole() {
        return UserRole.STUDENT;
    }

    @Override
    public String getInfo() {
        return super.getInfo()
                + ", Student Number: " + studentNumber
                + ", Group: " + groupName;
    }
}