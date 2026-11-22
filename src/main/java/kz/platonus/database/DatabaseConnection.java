package kz.platonus.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides connections to the SQLite database.
 */
public class DatabaseConnection {

    private static final String DATABASE_URL = "jdbc:sqlite:platonus.db";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
}