package kz.platonus.dao;

import kz.platonus.database.DatabaseConnection;
import kz.platonus.model.Lesson;
import kz.platonus.model.Course;
import kz.platonus.model.Teacher;
import kz.platonus.model.StudentGroup;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

public class LessonDAO {

    public void addLesson(Lesson lesson) {
        String sql = """
                INSERT INTO lessons
                (id, course_id, teacher_id, group_id, lesson_date,
                 start_time, end_time, room)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, lesson.getId());
            statement.setInt(2, lesson.getCourse().getId());
            statement.setInt(3, lesson.getTeacher().getId());
            statement.setInt(4, lesson.getGroup().getId());
            statement.setString(5, lesson.getDate().toString());
            statement.setString(6, lesson.getStartTime().toString());
            statement.setString(7, lesson.getEndTime().toString());
            statement.setString(8, lesson.getRoom());

            statement.executeUpdate();

            System.out.println("Lesson added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding lesson: " + e.getMessage());
        }
    }

    public Lesson findById(int id) {
        String sql = """
                SELECT l.*, 
                       c.course_name, c.course_code, c.credits,
                       u.full_name, u.email,
                       t.department, t.academic_degree,
                       g.group_name, g.specialty
                FROM lessons l
                JOIN courses c ON l.course_id = c.id
                JOIN teachers t ON l.teacher_id = t.id
                JOIN users u ON t.user_id = u.id
                JOIN student_groups g ON l.group_id = g.id
                WHERE l.id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return mapLesson(rs);
            }

        } catch (SQLException e) {
            System.out.println("Error finding lesson: " + e.getMessage());
        }

        return null;
    }

    public List<Lesson> findAll() {
        List<Lesson> lessons = new ArrayList<>();

        String sql = """
                SELECT l.*, 
                       c.course_name, c.course_code, c.credits,
                       u.full_name, u.email,
                       t.department, t.academic_degree,
                       g.group_name, g.specialty
                FROM lessons l
                JOIN courses c ON l.course_id = c.id
                JOIN teachers t ON l.teacher_id = t.id
                JOIN users u ON t.user_id = u.id
                JOIN student_groups g ON l.group_id = g.id
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                lessons.add(mapLesson(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving lessons: " + e.getMessage());
        }

        return lessons;
    }

    public void updateLesson(Lesson lesson) {
        String sql = """
                UPDATE lessons
                SET course_id = ?, teacher_id = ?, group_id = ?,
                    lesson_date = ?, start_time = ?, end_time = ?, room = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, lesson.getCourse().getId());
            statement.setInt(2, lesson.getTeacher().getId());
            statement.setInt(3, lesson.getGroup().getId());
            statement.setString(4, lesson.getDate().toString());
            statement.setString(5, lesson.getStartTime().toString());
            statement.setString(6, lesson.getEndTime().toString());
            statement.setString(7, lesson.getRoom());
            statement.setInt(8, lesson.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating lesson: " + e.getMessage());
        }
    }

    public void deleteLesson(int id) {
        String sql = "DELETE FROM lessons WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting lesson: " + e.getMessage());
        }
    }

    private Lesson mapLesson(ResultSet rs) throws SQLException {
        Course course = new Course(
                rs.getInt("course_id"),
                rs.getString("course_name"),
                rs.getString("course_code"),
                rs.getInt("credits")
        );

        Teacher teacher = new Teacher(
                rs.getInt("teacher_id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("department"),
                rs.getString("academic_degree")
        );

        StudentGroup group = new StudentGroup(
                rs.getInt("group_id"),
                rs.getString("group_name"),
                rs.getString("specialty")
        );

        return new Lesson(
                rs.getInt("id"),
                course,
                teacher,
                group,
                LocalDate.parse(rs.getString("lesson_date")),
                LocalTime.parse(rs.getString("start_time")),
                LocalTime.parse(rs.getString("end_time")),
                rs.getString("room")
        );
    }
}