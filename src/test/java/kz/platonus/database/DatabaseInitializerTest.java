package kz.platonus.database;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseInitializerTest {

    @Test
    void shouldInitializeDatabaseSuccessfully() {
        assertDoesNotThrow(DatabaseInitializer::initialize);
    }

    @Test
    void shouldCreateUsersTable() throws Exception {
        DatabaseInitializer.initialize();

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(
                     "SELECT name FROM sqlite_master WHERE type='table' AND name='users'")) {

            assertTrue(result.next());
            assertEquals("users", result.getString("name"));
        }
    }

    @Test
    void shouldCreateAttendanceTable() throws Exception {
        DatabaseInitializer.initialize();

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(
                     "SELECT name FROM sqlite_master WHERE type='table' AND name='attendance'")) {

            assertTrue(result.next());
            assertEquals("attendance", result.getString("name"));
        }
    }

    @Test
    void shouldCreateGradesTable() throws Exception {
        DatabaseInitializer.initialize();

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(
                     "SELECT name FROM sqlite_master WHERE type='table' AND name='grades'")) {

            assertTrue(result.next());
            assertEquals("grades", result.getString("name"));
        }
    }
}