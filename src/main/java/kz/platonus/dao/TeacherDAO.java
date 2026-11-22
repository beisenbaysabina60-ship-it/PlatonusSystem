package kz.platonus.dao;

import kz.platonus.database.DatabaseConnection;
import kz.platonus.model.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {

    public void addTeacher(Teacher teacher) {
        String userSql = "INSERT INTO users (id, full_name, email, role) VALUES (?, ?, ?, ?)";
        String teacherSql = "INSERT INTO teachers (id, user_id, department, academic_degree) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement userStatement = connection.prepareStatement(userSql);
                 PreparedStatement teacherStatement = connection.prepareStatement(teacherSql)) {

                userStatement.setInt(1, teacher.getId());
                userStatement.setString(2, teacher.getFullName());
                userStatement.setString(3, teacher.getEmail());
                userStatement.setString(4, teacher.getRole().name());
                userStatement.executeUpdate();

                teacherStatement.setInt(1, teacher.getId());
                teacherStatement.setInt(2, teacher.getId());
                teacherStatement.setString(3, teacher.getDepartment());
                teacherStatement.setString(4, teacher.getAcademicDegree());
                teacherStatement.executeUpdate();

                connection.commit();
                System.out.println("Teacher added successfully!");

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.out.println("Error adding teacher: " + e.getMessage());
        }
    }

    public Teacher findById(int id) {
        String sql = """
                SELECT u.*, t.department, t.academic_degree
                FROM users u
                JOIN teachers t ON u.id = t.user_id
                WHERE t.id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new Teacher(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("department"),
                        rs.getString("academic_degree")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error finding teacher: " + e.getMessage());
        }

        return null;
    }

    public List<Teacher> findAll() {
        List<Teacher> teachers = new ArrayList<>();

        String sql = """
                SELECT u.*, t.department, t.academic_degree
                FROM users u
                JOIN teachers t ON u.id = t.user_id
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                teachers.add(new Teacher(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("department"),
                        rs.getString("academic_degree")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving teachers: " + e.getMessage());
        }

        return teachers;
    }

    public void updateTeacher(Teacher teacher) {
        String userSql = "UPDATE users SET full_name = ?, email = ? WHERE id = ?";
        String teacherSql = "UPDATE teachers SET department = ?, academic_degree = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement userStatement = connection.prepareStatement(userSql);
                 PreparedStatement teacherStatement = connection.prepareStatement(teacherSql)) {

                userStatement.setString(1, teacher.getFullName());
                userStatement.setString(2, teacher.getEmail());
                userStatement.setInt(3, teacher.getId());
                userStatement.executeUpdate();

                teacherStatement.setString(1, teacher.getDepartment());
                teacherStatement.setString(2, teacher.getAcademicDegree());
                teacherStatement.setInt(3, teacher.getId());
                teacherStatement.executeUpdate();

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.out.println("Error updating teacher: " + e.getMessage());
        }
    }

    public void deleteTeacher(int id) {
        String teacherSql = "DELETE FROM teachers WHERE id = ?";
        String userSql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement teacherStatement = connection.prepareStatement(teacherSql);
                 PreparedStatement userStatement = connection.prepareStatement(userSql)) {

                teacherStatement.setInt(1, id);
                teacherStatement.executeUpdate();

                userStatement.setInt(1, id);
                userStatement.executeUpdate();

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.out.println("Error deleting teacher: " + e.getMessage());
        }
    }
}