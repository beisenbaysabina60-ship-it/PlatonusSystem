package kz.platonus.schedule;

import kz.platonus.model.Lesson;

import java.util.ArrayList;
import java.util.List;

/** Checks timetable overlaps for a proposed lesson. */
public class ScheduleConflictService {

    /** Returns human-readable conflicts with already scheduled lessons. */
    public List<String> findConflicts(Lesson candidate, List<Lesson> existingLessons) {
        if (candidate == null || existingLessons == null) {
            throw new IllegalArgumentException("Candidate and lesson list are required");
        }

        List<String> conflicts = new ArrayList<>();
        for (Lesson existing : existingLessons) {
            if (existing == null || existing.getId() == candidate.getId()) {
                continue;
            }
            if (!existing.getDate().equals(candidate.getDate()) || !overlaps(candidate, existing)) {
                continue;
            }

            if (existing.getTeacher().getId() == candidate.getTeacher().getId()) {
                conflicts.add("Teacher is already assigned to another lesson");
            }
            if (existing.getGroup().getId() == candidate.getGroup().getId()) {
                conflicts.add("Student group already has a lesson at this time");
            }
            if (existing.getRoom().equalsIgnoreCase(candidate.getRoom())) {
                conflicts.add("Classroom is already occupied");
            }
        }
        return conflicts;
    }

    public boolean isAvailable(Lesson candidate, List<Lesson> existingLessons) {
        return findConflicts(candidate, existingLessons).isEmpty();
    }

    private boolean overlaps(Lesson first, Lesson second) {
        return first.getStartTime().isBefore(second.getEndTime())
                && second.getStartTime().isBefore(first.getEndTime());
    }
}
