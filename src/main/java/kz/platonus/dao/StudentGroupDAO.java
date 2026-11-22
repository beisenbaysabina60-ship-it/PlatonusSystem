package kz.platonus.dao;

import kz.platonus.model.StudentGroup;
import kz.platonus.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentGroupDAO {

    public void addGroup(StudentGroup group) {
        String sql = "INSERT INTO student_groups (id, group_name, specialty) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, group.getId());
            statement.setString(2, group.getGroupName());
            statement.setString(3, group.getSpecialty());

            statement.executeUpdate();
            System.out.println("Group added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding group: " + e.getMessage());
        }
    }

    public StudentGroup findById(int id) {
        String sql = "SELECT * FROM student_groups WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new StudentGroup(
                        resultSet.getInt("id"),
                        resultSet.getString("group_name"),
                        resultSet.getString("specialty")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error finding group: " + e.getMessage());
        }

        return null;
    }

    public List<StudentGroup> findAll() {
        List<StudentGroup> groups = new ArrayList<>();
        String sql = "SELECT * FROM student_groups";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                groups.add(new StudentGroup(
                        resultSet.getInt("id"),
                        resultSet.getString("group_name"),
                        resultSet.getString("specialty")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving groups: " + e.getMessage());
        }

        return groups;
    }

    public void updateGroup(StudentGroup group) {
        String sql = "UPDATE student_groups SET group_name = ?, specialty = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, group.getGroupName());
            statement.setString(2, group.getSpecialty());
            statement.setInt(3, group.getId());

            statement.executeUpdate();
            System.out.println("Group updated successfully!");

        } catch (SQLException e) {
            System.out.println("Error updating group: " + e.getMessage());
        }
    }

    public void deleteGroup(int id) {
        String sql = "DELETE FROM student_groups WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Group deleted successfully!");

        } catch (SQLException e) {
            System.out.println("Error deleting group: " + e.getMessage());
        }
    }
}