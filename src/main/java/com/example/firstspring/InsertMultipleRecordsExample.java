package com.example.firstspring;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class InsertMultipleRecordsExample {
    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String user = "postgres";
    private final String password = "Chestor25";

    private static final String INSERT_USERS_SQL = "INSERT INTO users" +
            "  (id, name, email, country, password) VALUES " +
            " (?, ?, ?, ?, ?);";

    public void insertUsers(List < User > list) {
        try (
                Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement statement = conn.prepareStatement(INSERT_USERS_SQL);) {
            int count = 0;

            for (User user: list) {
                statement.setInt(1, user.getId());
                statement.setString(2, user.getName());
                statement.setString(3, user.getEmail());
                statement.setString(4, user.getCountry());
                statement.setString(5, user.getPassword());

                statement.addBatch();
                count++;
                // Выполняется каждые 100 строк и меньше
                if (count % 100 == 0 || count == list.size()) {
                    statement.executeBatch();
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        InsertMultipleRecordsExample example = new InsertMultipleRecordsExample();
        example.insertUsers(Arrays.asList(new User(2, "Ramesh", "ramesh@gmail.com", "India", "password123"),
                new User(3, "John", "john@gmail.com", "US", "password123")));
    }


    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
