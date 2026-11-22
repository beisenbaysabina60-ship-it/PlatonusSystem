package kz.platonus.model;

import java.time.LocalDateTime;

/**
 * Represents a student's attendance record.
 */
public class Attendance {
    private final int id;
    private final Student student;
    private final Lesson lesson;
    private AttendanceStatus status;
    private LocalDateTime recordedAt;
    private String comment;

    public Attendance(int id, Student student, Lesson lesson,
                     AttendanceStatus status, String comment) {

        if (id <= 0) {
            throw new IllegalArgumentException("Attendance ID must be positive");
        }
        if (student == null || lesson == null || status == null) {
            throw new IllegalArgumentException(
                    "Student, lesson and status are required");
        }

        this.id = id;
        this.student = student;
        this.lesson = lesson;
        this.status = status;
        this.recordedAt = LocalDateTime.now();
        this.comment = comment == null ? "" : comment;
    }

    public int getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public String getComment() {
        return comment;
    }

    public void setStatus(AttendanceStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = status;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? "" : comment;
    }

    public void updateRecordedAt() {
        this.recordedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", student=" + student.getFullName() +
                ", lesson=" + lesson.getCourse().getCourseName() +
                ", status=" + status +
                ", recordedAt=" + recordedAt +
                ", comment='" + comment + '\'' +
                '}';
    }
}