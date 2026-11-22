package kz.platonus.model;

/**
 * Represents a student's grade for a course.
 */
public class Grade {
    private final int id;
    private final Student student;
    private final Course course;
    private double score;
    private String letterGrade;

    public Grade(int id, Student student, Course course, double score) {
        if (id <= 0) {
            throw new IllegalArgumentException("Grade ID must be positive");
        }
        if (student == null || course == null) {
            throw new IllegalArgumentException("Student and course are required");
        }
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }

        this.id = id;
        this.student = student;
        this.course = course;
        this.score = score;
        this.letterGrade = calculateLetterGrade(score);
    }

    private String calculateLetterGrade(double score) {
        if (score >= 95) return "A";
        if (score >= 90) return "A-";
        if (score >= 85) return "B+";
        if (score >= 80) return "B";
        if (score >= 75) return "B-";
        if (score >= 70) return "C+";
        if (score >= 65) return "C";
        if (score >= 60) return "C-";
        if (score >= 50) return "D";
        return "F";
    }

    public int getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public double getScore() {
        return score;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public void setScore(double score) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }
        this.score = score;
        this.letterGrade = calculateLetterGrade(score);
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", student=" + student.getFullName() +
                ", course=" + course.getCourseName() +
                ", score=" + score +
                ", letterGrade='" + letterGrade + '\'' +
                '}';
    }
}