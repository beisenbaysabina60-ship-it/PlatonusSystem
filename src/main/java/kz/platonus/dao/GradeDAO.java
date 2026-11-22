package kz.platonus.dao;

import kz.platonus.database.DatabaseConnection;
import kz.platonus.model.Grade;
import kz.platonus.model.Student;
import kz.platonus.model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDAO {

    // ADD
    public void addGrade(Grade grade) {
        String sql = "INSERT INTO grades (id, student_id, course_id, score, letter_grade) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, grade.getId());
            statement.setInt(2, grade.getStudent().getId());
            statement.setInt(3, grade.getCourse().getId());
            statement.setDouble(4, grade.getScore());
            statement.setString(5, grade.getLetterGrade());

            statement.executeUpdate();
            System.out.println("Grade added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding grade: " + e.getMessage());
        }
    }

    // FIND BY ID
    public Grade findById(int id) {
        String sql = """
                SELECT g.id, g.student_id, g.course_id, g.score,
                       u.full_name, u.email,
                       s.student_number, s.group_name,
                       c.course_name, c.course_code, c.credits
                FROM grades g
                JOIN students s ON g.student_id = s.id
                JOIN users u ON s.user_id = u.id
                JOIN courses c ON g.course_id = c.id
                WHERE g.id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return mapGrade(rs);
            }

        } catch (SQLException e) {
            System.out.println("Error finding grade: " + e.getMessage());
        }

        return null;
    }

    // FIND ALL
    public List<Grade> findAll() {
        List<Grade> grades = new ArrayList<>();

        String sql = """
                SELECT g.id, g.student_id, g.course_id, g.score,
                       u.full_name, u.email,
                       s.student_number, s.group_name,
                       c.course_name, c.course_code, c.credits
                FROM grades g
                JOIN students s ON g.student_id = s.id
                JOIN users u ON s.user_id = u.id
                JOIN courses c ON g.course_id = c.id
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                grades.add(mapGrade(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving grades: " + e.getMessage());
        }

        return grades;
    }

    // UPDATE
    public void updateGrade(Grade grade) {
        String sql = """
                UPDATE grades
                SET student_id = ?, course_id = ?, score = ?, letter_grade = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, grade.getStudent().getId());
            statement.setInt(2, grade.getCourse().getId());
            statement.setDouble(3, grade.getScore());
            statement.setString(4, grade.getLetterGrade());
            statement.setInt(5, grade.getId());

            statement.executeUpdate();
            System.out.println("Grade updated successfully!");

        } catch (SQLException e) {
            System.out.println("Error updating grade: " + e.getMessage());
        }
    }

    // DELETE
    public void deleteGrade(int id) {
        String sql = "DELETE FROM grades WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();

            System.out.println("Grade deleted successfully!");

        } catch (SQLException e) {
            System.out.println("Error deleting grade: " + e.getMessage());
        }
    }

    // FIND BY STUDENT
    public List<Grade> findByStudentId(int studentId) {
        List<Grade> grades = new ArrayList<>();

        String sql = """
                SELECT g.id, g.student_id, g.course_id, g.score,
                       u.full_name, u.email,
                       s.student_number, s.group_name,
                       c.course_name, c.course_code, c.credits
                FROM grades g
                JOIN students s ON g.student_id = s.id
                JOIN users u ON s.user_id = u.id
                JOIN courses c ON g.course_id = c.id
                WHERE g.student_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                grades.add(mapGrade(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error finding student grades: " + e.getMessage());
        }

        return grades;
    }

    // FIND BY COURSE
    public List<Grade> findByCourseId(int courseId) {
        List<Grade> grades = new ArrayList<>();

        String sql = """
                SELECT g.id, g.student_id, g.course_id, g.score,
                       u.full_name, u.email,
                       s.student_number, s.group_name,
                       c.course_name, c.course_code, c.credits
                FROM grades g
                JOIN students s ON g.student_id = s.id
                JOIN users u ON s.user_id = u.id
                JOIN courses c ON g.course_id = c.id
                WHERE g.course_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, courseId);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                grades.add(mapGrade(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error finding course grades: " + e.getMessage());
        }

        return grades;
    }

    // MAP RESULT
    private Grade mapGrade(ResultSet rs) throws SQLException {

        Student student = new Student(
                rs.getInt("student_id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("student_number"),
                rs.getString("group_name")
        );

        Course course = new Course(
                rs.getInt("course_id"),
                rs.getString("course_name"),
                rs.getString("course_code"),
                rs.getInt("credits")
        );

        return new Grade(
                rs.getInt("id"),
                student,
                course,
                rs.getDouble("score")
        );
    }
}