package kz.platonus.model;

/**
 * Represents a university course in the Platonus system.
 */
public class Course {

    private final int id;
    private String courseName;
    private String courseCode;
    private int credits;

    public Course(int id, String courseName,
                  String courseCode, int credits) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Course ID must be positive");
        }

        if (courseName == null || courseName.isBlank()) {
            throw new IllegalArgumentException(
                    "Course name cannot be empty");
        }

        if (courseCode == null || courseCode.isBlank()) {
            throw new IllegalArgumentException(
                    "Course code cannot be empty");
        }

        if (credits <= 0) {
            throw new IllegalArgumentException(
                    "Credits must be positive");
        }

        this.id = id;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.credits = credits;
    }

    public int getId() {
        return id;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public int getCredits() {
        return credits;
    }

    public void setCourseName(String courseName) {
        if (courseName == null || courseName.isBlank()) {
            throw new IllegalArgumentException(
                    "Course name cannot be empty");
        }

        this.courseName = courseName;
    }

    public void setCourseCode(String courseCode) {
        if (courseCode == null || courseCode.isBlank()) {
            throw new IllegalArgumentException(
                    "Course code cannot be empty");
        }

        this.courseCode = courseCode;
    }

    public void setCredits(int credits) {
        if (credits <= 0) {
            throw new IllegalArgumentException(
                    "Credits must be positive");
        }

        this.credits = credits;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", credits=" + credits +
                '}';
    }
}