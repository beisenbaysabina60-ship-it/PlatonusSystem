package kz.platonus.database;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {

    @FunctionalInterface
    public interface TransactionOperation {
        void execute(Connection connection) throws SQLException;
    }

    public static void executeTransaction(TransactionOperation operation) {
        try (Connection connection = DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                operation.execute(connection);

                connection.commit();

            } catch (SQLException | RuntimeException e) {

                connection.rollback();

                throw new RuntimeException(
                        "Transaction failed. Changes rolled back.",
                        e
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database transaction error", e);
        }
    }
}