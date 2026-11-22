package kz.platonus.dao;

import kz.platonus.database.DatabaseConnection;
import kz.platonus.model.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    public void addAdmin(Admin admin) {
        String userSql = "INSERT INTO users (id, full_name, email, role) VALUES (?, ?, ?, ?)";
        String adminSql = "INSERT INTO admins (id, user_id, admin_level) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement userStatement = connection.prepareStatement(userSql);
                 PreparedStatement adminStatement = connection.prepareStatement(adminSql)) {

                userStatement.setInt(1, admin.getId());
                userStatement.setString(2, admin.getFullName());
                userStatement.setString(3, admin.getEmail());
                userStatement.setString(4, admin.getRole().name());
                userStatement.executeUpdate();

                adminStatement.setInt(1, admin.getId());
                adminStatement.setInt(2, admin.getId());
                adminStatement.setString(3, admin.getAdminLevel());
                adminStatement.executeUpdate();

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.out.println("Error adding admin: " + e.getMessage());
        }
    }

    public Admin findById(int id) {
        String sql = """
                SELECT u.*, a.admin_level
                FROM users u
                JOIN admins a ON u.id = a.user_id
                WHERE a.id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new Admin(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("admin_level")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error finding admin: " + e.getMessage());
        }

        return null;
    }

    public List<Admin> findAll() {
        List<Admin> admins = new ArrayList<>();

        String sql = """
                SELECT u.*, a.admin_level
                FROM users u
                JOIN admins a ON u.id = a.user_id
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                admins.add(new Admin(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("admin_level")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving admins: " + e.getMessage());
        }

        return admins;
    }

    public void updateAdmin(Admin admin) {
        String userSql = "UPDATE users SET full_name = ?, email = ? WHERE id = ?";
        String adminSql = "UPDATE admins SET admin_level = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement userStatement = connection.prepareStatement(userSql);
                 PreparedStatement adminStatement = connection.prepareStatement(adminSql)) {

                userStatement.setString(1, admin.getFullName());
                userStatement.setString(2, admin.getEmail());
                userStatement.setInt(3, admin.getId());
                userStatement.executeUpdate();

                adminStatement.setString(1, admin.getAdminLevel());
                adminStatement.setInt(2, admin.getId());
                adminStatement.executeUpdate();

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.out.println("Error updating admin: " + e.getMessage());
        }
    }

    public void deleteAdmin(int id) {
        String adminSql = "DELETE FROM admins WHERE id = ?";
        String userSql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement adminStatement = connection.prepareStatement(adminSql);
                 PreparedStatement userStatement = connection.prepareStatement(userSql)) {

                adminStatement.setInt(1, id);
                adminStatement.executeUpdate();

                userStatement.setInt(1, id);
                userStatement.executeUpdate();

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.out.println("Error deleting admin: " + e.getMessage());
        }
    }
}