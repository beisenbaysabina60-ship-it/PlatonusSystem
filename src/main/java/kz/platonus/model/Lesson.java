package kz.platonus.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a university lesson.
 */
public class Lesson {
    private final int id;
    private final Course course;
    private final Teacher teacher;
    private final StudentGroup group;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String room;

    public Lesson(int id, Course course, Teacher teacher,
                  StudentGroup group, LocalDate date,
                  LocalTime startTime, LocalTime endTime,
                  String room) {

        if (id <= 0) {
            throw new IllegalArgumentException("Lesson ID must be positive");
        }
        if (course == null || teacher == null || group == null) {
            throw new IllegalArgumentException("Course, teacher and group are required");
        }
        if (date == null || startTime == null || endTime == null) {
            throw new IllegalArgumentException("Date and time are required");
        }
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        if (room == null || room.isBlank()) {
            throw new IllegalArgumentException("Room cannot be empty");
        }

        this.id = id;
        this.course = course;
        this.teacher = teacher;
        this.group = group;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
    }

    public int getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public StudentGroup getGroup() {
        return group;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getRoom() {
        return room;
    }

    public void setDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        this.date = date;
    }

    public void setStartTime(LocalTime startTime) {
        if (startTime == null || !startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Invalid start time");
        }
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        if (endTime == null || !endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("Invalid end time");
        }
        this.endTime = endTime;
    }

    public void setRoom(String room) {
        if (room == null || room.isBlank()) {
            throw new IllegalArgumentException("Room cannot be empty");
        }
        this.room = room;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", course=" + course.getCourseName() +
                ", teacher=" + teacher.getFullName() +
                ", group=" + group.getGroupName() +
                ", date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", room='" + room + '\'' +
                '}';
    }
}