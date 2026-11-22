package kz.platonus.model;

/**
 * Represents a teacher in the Platonus system.
 */
public class Teacher extends User {

    private String department;
    private String academicDegree;

    public Teacher(int id, String fullName, String email,
                   String department, String academicDegree) {

        super(id, fullName, email);

        if (department == null || department.isBlank()) {
            throw new IllegalArgumentException(
                    "Department cannot be empty");
        }

        this.department = department;
        this.academicDegree = academicDegree;
    }

    public String getDepartment() {
        return department;
    }

    public String getAcademicDegree() {
        return academicDegree;
    }

    public void setDepartment(String department) {
        if (department == null || department.isBlank()) {
            throw new IllegalArgumentException(
                    "Department cannot be empty");
        }
        this.department = department;
    }

    public void setAcademicDegree(String academicDegree) {
        this.academicDegree = academicDegree;
    }

    @Override
    public UserRole getRole() {
        return UserRole.TEACHER;
    }

    @Override
    public String getInfo() {
        return super.getInfo()
                + ", Department: " + department
                + ", Academic Degree: " + academicDegree;
    }
}