package kz.platonus.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Initializes the SQLite database and creates required tables.
 */
public class DatabaseInitializer {

    public static void initialize() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("PRAGMA foreign_keys = ON");

            // Users table
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY,
                        full_name TEXT NOT NULL,
                        email TEXT NOT NULL UNIQUE,
                        role TEXT NOT NULL
                    )
                    """);

            // Students table
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS students (
                        id INTEGER PRIMARY KEY,
                        user_id INTEGER NOT NULL UNIQUE,
                        student_number TEXT NOT NULL UNIQUE,
                        group_name TEXT NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES users(id)
                    )
                    """);

            // Teachers table
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS teachers (
                        id INTEGER PRIMARY KEY,
                        user_id INTEGER NOT NULL UNIQUE,
                        department TEXT NOT NULL,
                        academic_degree TEXT,
                        FOREIGN KEY (user_id) REFERENCES users(id)
                    )
                    """);

            // Admins table
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS admins (
                        id INTEGER PRIMARY KEY,
                        user_id INTEGER NOT NULL UNIQUE,
                        admin_level TEXT NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES users(id)
                    )
                    """);

            // Courses table
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS courses (
                        id INTEGER PRIMARY KEY,
                        course_name TEXT NOT NULL,
                        course_code TEXT NOT NULL UNIQUE,
                        credits INTEGER NOT NULL CHECK (credits > 0)
                    )
                    """);

            // Student groups table
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS student_groups (
                        id INTEGER PRIMARY KEY,
                        group_name TEXT NOT NULL UNIQUE,
                        specialty TEXT NOT NULL
                    )
                    """);

            // Lessons table
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS lessons (
                        id INTEGER PRIMARY KEY,
                        course_id INTEGER NOT NULL,
                        teacher_id INTEGER NOT NULL,
                        group_id INTEGER NOT NULL,
                        lesson_date TEXT NOT NULL,
                        start_time TEXT NOT NULL,
                        end_time TEXT NOT NULL,
                        room TEXT NOT NULL,
                        FOREIGN KEY (course_id) REFERENCES courses(id),
                        FOREIGN KEY (teacher_id) REFERENCES teachers(id),
                        FOREIGN KEY (group_id) REFERENCES student_groups(id)
                    )
                    """);

            // Attendance table
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS attendance (
                        id INTEGER PRIMARY KEY,
                        student_id INTEGER NOT NULL,
                        lesson_id INTEGER NOT NULL,
                        status TEXT NOT NULL,
                        recorded_at TEXT NOT NULL,
                        comment TEXT,
                        UNIQUE (student_id, lesson_id),
                        FOREIGN KEY (student_id) REFERENCES students(id),
                        FOREIGN KEY (lesson_id) REFERENCES lessons(id)
                    )
                    """);

            // Grades table
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS grades (
                        id INTEGER PRIMARY KEY,
                        student_id INTEGER NOT NULL,
                        course_id INTEGER NOT NULL,
                        score REAL NOT NULL CHECK (score >= 0 AND score <= 100),
                        letter_grade TEXT NOT NULL,
                        UNIQUE (student_id, course_id),
                        FOREIGN KEY (student_id) REFERENCES students(id),
                        FOREIGN KEY (course_id) REFERENCES courses(id)
                    )
                    """);

            System.out.println("Database initialized successfully!");

        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}