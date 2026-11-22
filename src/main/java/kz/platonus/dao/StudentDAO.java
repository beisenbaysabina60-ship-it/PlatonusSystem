package kz.platonus.dao;

import kz.platonus.database.DatabaseConnection;
import kz.platonus.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student.
 * Provides CRUD operations for students.
 */
public class StudentDAO {

    // CREATE
    public void addStudent(Student student) throws SQLException {
        String userSql = """
                INSERT INTO users (id, full_name, email, role)
                VALUES (?, ?, ?, ?)
                """;

        String studentSql = """
                INSERT INTO students (id, user_id, student_number, group_name)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                // Insert into users table
                try (PreparedStatement userStatement =
                             connection.prepareStatement(userSql)) {

                    userStatement.setInt(1, student.getId());
                    userStatement.setString(2, student.getFullName());
                    userStatement.setString(3, student.getEmail());
                    userStatement.setString(4, "STUDENT");

                    userStatement.executeUpdate();
                }

                // Insert into students table
                try (PreparedStatement studentStatement =
                             connection.prepareStatement(studentSql)) {

                    studentStatement.setInt(1, student.getId());
                    studentStatement.setInt(2, student.getId());
                    studentStatement.setString(3, student.getStudentNumber());
                    studentStatement.setString(4, student.getGroupName());

                    studentStatement.executeUpdate();
                }

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    // READ BY ID
    public Student findById(int id) throws SQLException {
        String sql = """
                SELECT u.id, u.full_name, u.email,
                       s.student_number, s.group_name
                FROM users u
                JOIN students s ON u.id = s.user_id
                WHERE s.id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapStudent(result);
                }
            }
        }

        return null;
    }

    // READ ALL
    public List<Student> findAll() throws SQLException {
        List<Student> students = new ArrayList<>();

        String sql = """
                SELECT u.id, u.full_name, u.email,
                       s.student_number, s.group_name
                FROM users u
                JOIN students s ON u.id = s.user_id
                ORDER BY u.id
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {
                students.add(mapStudent(result));
            }
        }

        return students;
    }

    // UPDATE
    public boolean updateStudent(Student student) throws SQLException {
        String userSql = """
                UPDATE users
                SET full_name = ?, email = ?
                WHERE id = ?
                """;

        String studentSql = """
                UPDATE students
                SET student_number = ?, group_name = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                int userRows;

                try (PreparedStatement userStatement =
                             connection.prepareStatement(userSql)) {

                    userStatement.setString(1, student.getFullName());
                    userStatement.setString(2, student.getEmail());
                    userStatement.setInt(3, student.getId());

                    userRows = userStatement.executeUpdate();
                }

                try (PreparedStatement studentStatement =
                             connection.prepareStatement(studentSql)) {

                    studentStatement.setString(1, student.getStudentNumber());
                    studentStatement.setString(2, student.getGroupName());
                    studentStatement.setInt(3, student.getId());

                    studentStatement.executeUpdate();
                }

                connection.commit();
                return userRows > 0;

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    // DELETE
    public boolean deleteStudent(int id) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                String studentSql = "DELETE FROM students WHERE id = ?";
                String userSql = "DELETE FROM users WHERE id = ?";

                int deletedRows;

                try (PreparedStatement studentStatement =
                             connection.prepareStatement(studentSql)) {

                    studentStatement.setInt(1, id);
                    deletedRows = studentStatement.executeUpdate();
                }

                if (deletedRows > 0) {
                    try (PreparedStatement userStatement =
                                 connection.prepareStatement(userSql)) {

                        userStatement.setInt(1, id);
                        userStatement.executeUpdate();
                    }
                }

                connection.commit();
                return deletedRows > 0;

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    // Convert ResultSet to Student object
    private Student mapStudent(ResultSet result) throws SQLException {
        return new Student(
                result.getInt("id"),
                result.getString("full_name"),
                result.getString("email"),
                result.getString("student_number"),
                result.getString("group_name")
        );
    }
}