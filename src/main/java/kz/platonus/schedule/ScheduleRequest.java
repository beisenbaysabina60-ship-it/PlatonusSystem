package kz.platonus.schedule;

import kz.platonus.model.Course;
import kz.platonus.model.StudentGroup;
import kz.platonus.model.Teacher;

/** One lesson that must be placed into the timetable. */
public record ScheduleRequest(Course course, Teacher teacher,
                              StudentGroup group, int durationMinutes) {
    public ScheduleRequest {
        if (course == null || teacher == null || group == null) {
            throw new IllegalArgumentException("Course, teacher and group are required");
        }
        if (durationMinutes <= 0 || durationMinutes > 240) {
            throw new IllegalArgumentException("Duration must be between 1 and 240 minutes");
        }
    }
}
