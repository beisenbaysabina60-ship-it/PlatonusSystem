package kz.platonus.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class TransactionManagerTest {

    @BeforeEach
    void setUp() throws Exception {
        DatabaseInitializer.initialize();
        cleanData();
    }

    @AfterEach
    void tearDown() throws Exception {
        cleanData();
    }

    private void cleanData() throws Exception {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM courses WHERE id IN (13001, 13002, 13003, 13004)")) {
            statement.executeUpdate();
        }
    }

    // SQL қатесін RuntimeException ретінде өңдейді
    private boolean courseExists(int id) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id FROM courses WHERE id = ?")) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testTransactionCommit() {

        TransactionManager.executeTransaction(connection -> {

            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO courses (id, course_name, course_code, credits) VALUES (?, ?, ?, ?)")) {

                statement.setInt(1, 13001);
                statement.setString(2, "Transaction Commit Test");
                statement.setString(3, "TRANSCOMMIT13001");
                statement.setInt(4, 3);

                statement.executeUpdate();
            }
        });

        assertTrue(courseExists(13001));
    }

    @Test
    void testTransactionRollback() {

        assertThrows(RuntimeException.class, () -> {

            TransactionManager.executeTransaction(connection -> {

                try (PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO courses (id, course_name, course_code, credits) VALUES (?, ?, ?, ?)")) {

                    statement.setInt(1, 13002);
                    statement.setString(2, "Transaction Rollback Test");
                    statement.setString(3, "TRANSROLLBACK13002");
                    statement.setInt(4, 3);

                    statement.executeUpdate();
                }

                throw new SQLException("Intentional test error");
            });
        });

        assertFalse(courseExists(13002));
    }

    @Test
    void testTransactionWithMultipleOperations() {

        TransactionManager.executeTransaction(connection -> {

            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO courses (id, course_name, course_code, credits) VALUES (?, ?, ?, ?)")) {

                statement.setInt(1, 13003);
                statement.setString(2, "First Transaction Course");
                statement.setString(3, "TRANS13003");
                statement.setInt(4, 3);

                statement.executeUpdate();

                statement.setInt(1, 13004);
                statement.setString(2, "Second Transaction Course");
                statement.setString(3, "TRANS13004");
                statement.setInt(4, 4);

                statement.executeUpdate();
            }
        });

        assertTrue(courseExists(13003));
        assertTrue(courseExists(13004));
    }

    @Test
    void testRollbackRemovesAllChanges() {

        assertThrows(RuntimeException.class, () -> {

            TransactionManager.executeTransaction(connection -> {

                try (PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO courses (id, course_name, course_code, credits) VALUES (?, ?, ?, ?)")) {

                    statement.setInt(1, 13003);
                    statement.setString(2, "Rollback First Course");
                    statement.setString(3, "ROLLBACK13003");
                    statement.setInt(4, 3);

                    statement.executeUpdate();

                    statement.setInt(1, 13004);
                    statement.setString(2, "Rollback Second Course");
                    statement.setString(3, "ROLLBACK13004");
                    statement.setInt(4, 3);

                    statement.executeUpdate();
                }

                throw new SQLException("Force rollback");
            });
        });

        assertFalse(courseExists(13003));
        assertFalse(courseExists(13004));
    }
}