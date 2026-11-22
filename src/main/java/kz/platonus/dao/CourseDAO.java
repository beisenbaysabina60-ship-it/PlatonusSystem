package kz.platonus.dao;

import kz.platonus.database.DatabaseConnection;
import kz.platonus.model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Course.
 * Provides CRUD operations.
 */
public class CourseDAO {

    // CREATE
    public void addCourse(Course course) throws SQLException {
        String sql = """
                INSERT INTO courses (id, course_name, course_code, credits)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, course.getId());
            statement.setString(2, course.getCourseName());
            statement.setString(3, course.getCourseCode());
            statement.setInt(4, course.getCredits());

            statement.executeUpdate();
        }
    }

    // READ BY ID
    public Course findById(int id) throws SQLException {
        String sql = """
                SELECT id, course_name, course_code, credits
                FROM courses
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapCourse(result);
                }
            }
        }

        return null;
    }

    // READ ALL
    public List<Course> findAll() throws SQLException {
        List<Course> courses = new ArrayList<>();

        String sql = """
                SELECT id, course_name, course_code, credits
                FROM courses
                ORDER BY id
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {
                courses.add(mapCourse(result));
            }
        }

        return courses;
    }

    // UPDATE
    public boolean updateCourse(Course course) throws SQLException {
        String sql = """
                UPDATE courses
                SET course_name = ?, course_code = ?, credits = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, course.getCourseName());
            statement.setString(2, course.getCourseCode());
            statement.setInt(3, course.getCredits());
            statement.setInt(4, course.getId());

            return statement.executeUpdate() > 0;
        }
    }

    // DELETE
    public boolean deleteCourse(int id) throws SQLException {
        String sql = "DELETE FROM courses WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;
        }
    }

    // Convert ResultSet to Course object
    private Course mapCourse(ResultSet result) throws SQLException {
        return new Course(
                result.getInt("id"),
                result.getString("course_name"),
                result.getString("course_code"),
                result.getInt("credits")
        );
    }
}