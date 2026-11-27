package kz.platonus.schedule;

import kz.platonus.model.Lesson;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/** Greedy timetable generator: places each request into the first available weekday/time/room. */
public class ScheduleGenerator {
    private static final LocalTime DAY_START = LocalTime.of(9, 0);
    private static final LocalTime DAY_END = LocalTime.of(18, 0);
    private static final int SLOT_STEP_MINUTES = 30;

    private final ScheduleConflictService conflictService;

    public ScheduleGenerator() {
        this(new ScheduleConflictService());
    }

    public ScheduleGenerator(ScheduleConflictService conflictService) {
        this.conflictService = conflictService;
    }

    public List<Lesson> generate(List<ScheduleRequest> requests,
                                 List<Lesson> existingLessons,
                                 LocalDate firstDay,
                                 int weekdaysToSearch,
                                 List<String> rooms,
                                 int firstLessonId) {
        if (requests == null || existingLessons == null || firstDay == null
                || rooms == null || rooms.isEmpty() || weekdaysToSearch <= 0
                || firstLessonId <= 0) {
            throw new IllegalArgumentException("Invalid schedule generation arguments");
        }

        List<Lesson> scheduled = new ArrayList<>();
        int nextId = firstLessonId;
        for (ScheduleRequest request : requests) {
            if (request == null) {
                throw new IllegalArgumentException("Schedule request cannot be null");
            }
            Lesson placed = null;
            for (int dayOffset = 0; dayOffset < weekdaysToSearch && placed == null; dayOffset++) {
                LocalDate date = firstDay.plusDays(dayOffset);
                if (date.getDayOfWeek().getValue() > 5) continue;

                for (LocalTime start = DAY_START;
                     !start.plusMinutes(request.durationMinutes()).isAfter(DAY_END) && placed == null;
                     start = start.plusMinutes(SLOT_STEP_MINUTES)) {
                    LocalTime end = start.plusMinutes(request.durationMinutes());
                    for (String room : rooms) {
                        if (room == null || room.isBlank()) continue;
                        Lesson candidate = new Lesson(nextId, request.course(), request.teacher(),
                                request.group(), date, start, end, room);
                        List<Lesson> all = new ArrayList<>(existingLessons);
                        all.addAll(scheduled);
                        if (conflictService.isAvailable(candidate, all)) {
                            placed = candidate;
                            break;
                        }
                    }
                }
            }
            if (placed == null) {
                throw new IllegalStateException("No available timetable slot for course: "
                        + request.course().getCourseName());
            }
            scheduled.add(placed);
            nextId++;
        }
        return scheduled;
    }
}
