package kz.platonus.service;

import kz.platonus.dao.AttendanceDAO;
import kz.platonus.model.Attendance;
import kz.platonus.model.AttendanceStatus;

import java.util.List;

public class AttendanceService {

    private final AttendanceDAO attendanceDAO;

    public AttendanceService() {
        this.attendanceDAO = new AttendanceDAO();
    }

    public AttendanceService(AttendanceDAO attendanceDAO) {
        this.attendanceDAO = attendanceDAO;
    }

    // Қатысуды тіркеу
    public void recordAttendance(Attendance attendance) {
        if (attendance == null) {
            throw new IllegalArgumentException("Attendance cannot be null");
        }

        attendanceDAO.addAttendance(attendance);
    }

    // Қатысу статусын өзгерту
    public void updateAttendanceStatus(
            int attendanceId,
            AttendanceStatus status
    ) {
        Attendance attendance = attendanceDAO.findById(attendanceId);

        if (attendance == null) {
            throw new IllegalArgumentException(
                    "Attendance not found: " + attendanceId
            );
        }

        if (status == null) {
            throw new IllegalArgumentException(
                    "Attendance status cannot be null"
            );
        }

        attendance.setStatus(status);
        attendance.updateRecordedAt();

        attendanceDAO.updateAttendance(attendance);
    }

    // Студенттің қатысу мәліметтерін алу
    public List<Attendance> getStudentAttendance(int studentId) {
        return attendanceDAO.findByStudentId(studentId);
    }

    // Сабақтың қатысу мәліметтерін алу
    public List<Attendance> getLessonAttendance(int lessonId) {
        return attendanceDAO.findByLessonId(lessonId);
    }

    // Қатысу пайызын есептеу
    public double calculateAttendancePercentage(int studentId) {
        List<Attendance> attendances =
                attendanceDAO.findByStudentId(studentId);

        if (attendances.isEmpty()) {
            return 0.0;
        }

        long attended = attendances.stream()
                .filter(a ->
                        a.getStatus() == AttendanceStatus.PRESENT ||
                        a.getStatus() == AttendanceStatus.LATE
                )
                .count();

        return (double) attended / attendances.size() * 100;
    }

    // Белгілі статус бойынша санау
    public long countByStatus(
            int studentId,
            AttendanceStatus status
    ) {
        return attendanceDAO.findByStudentId(studentId)
                .stream()
                .filter(a -> a.getStatus() == status)
                .count();
    }
}