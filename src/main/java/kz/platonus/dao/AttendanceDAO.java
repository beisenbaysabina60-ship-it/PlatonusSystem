package kz.platonus.dao;

import kz.platonus.database.DatabaseConnection;
import kz.platonus.model.Attendance;
import kz.platonus.model.AttendanceStatus;
import kz.platonus.model.Student;
import kz.platonus.model.Lesson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    public void addAttendance(Attendance attendance) {
        String sql = """
                INSERT INTO attendance
                (id, student_id, lesson_id, status, recorded_at, comment)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, attendance.getId());
            statement.setInt(2, attendance.getStudent().getId());
            statement.setInt(3, attendance.getLesson().getId());
            statement.setString(4, attendance.getStatus().name());
            statement.setString(5, attendance.getRecordedAt().toString());
            statement.setString(6, attendance.getComment());

            statement.executeUpdate();

            System.out.println("Attendance added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding attendance: " + e.getMessage());
        }
    }

    public Attendance findById(int id) {
        String sql = "SELECT * FROM attendance WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return mapAttendance(rs);
            }

        } catch (SQLException e) {
            System.out.println("Error finding attendance: " + e.getMessage());
        }

        return null;
    }

    public List<Attendance> findAll() {
        List<Attendance> attendances = new ArrayList<>();

        String sql = "SELECT * FROM attendance";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Attendance attendance = mapAttendance(rs);

                if (attendance != null) {
                    attendances.add(attendance);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving attendance: " + e.getMessage());
        }

        return attendances;
    }

    public void updateAttendance(Attendance attendance) {
        String sql = """
                UPDATE attendance
                SET student_id = ?, lesson_id = ?, status = ?,
                    recorded_at = ?, comment = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, attendance.getStudent().getId());
            statement.setInt(2, attendance.getLesson().getId());
            statement.setString(3, attendance.getStatus().name());
            statement.setString(4, attendance.getRecordedAt().toString());
            statement.setString(5, attendance.getComment());
            statement.setInt(6, attendance.getId());

            statement.executeUpdate();

            System.out.println("Attendance updated successfully!");

        } catch (SQLException e) {
            System.out.println("Error updating attendance: " + e.getMessage());
        }
    }

    public void deleteAttendance(int id) {
        String sql = "DELETE FROM attendance WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();

            System.out.println("Attendance deleted successfully!");

        } catch (SQLException e) {
            System.out.println("Error deleting attendance: " + e.getMessage());
        }
    }

    public List<Attendance> findByStudentId(int studentId) {
        List<Attendance> attendances = new ArrayList<>();

        String sql = "SELECT * FROM attendance WHERE student_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Attendance attendance = mapAttendance(rs);

                if (attendance != null) {
                    attendances.add(attendance);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding student attendance: " + e.getMessage());
        }

        return attendances;
    }

    public List<Attendance> findByLessonId(int lessonId) {
        List<Attendance> attendances = new ArrayList<>();

        String sql = "SELECT * FROM attendance WHERE lesson_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, lessonId);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Attendance attendance = mapAttendance(rs);

                if (attendance != null) {
                    attendances.add(attendance);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding lesson attendance: " + e.getMessage());
        }

        return attendances;
    }

    private Attendance mapAttendance(ResultSet rs) throws SQLException {
        StudentDAO studentDAO = new StudentDAO();
        LessonDAO lessonDAO = new LessonDAO();

        Student student = studentDAO.findById(rs.getInt("student_id"));
        Lesson lesson = lessonDAO.findById(rs.getInt("lesson_id"));

        if (student == null || lesson == null) {
            return null;
        }

        Attendance attendance = new Attendance(
                rs.getInt("id"),
                student,
                lesson,
                AttendanceStatus.valueOf(rs.getString("status")),
                rs.getString("comment")
        );

        return attendance;
    }
}