package com.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private Connection connection;

    public void connect() throws SQLException {
        String url = "jdbc:sqlserver://dbsys.cs.vsb.cz\\sqldb;databaseName=MEL0104;encrypt=true;trustServerCertificate=true";
        String username = "MEL0104";
        String password = "Nx7K4OrE5NnA135e";

        connection = DriverManager.getConnection(url, username, password);
        connection.setAutoCommit(true);
        System.out.println(" Connected to the database");
    }

    public void beginTransaction() throws SQLException {
        if (connection != null) {
            connection.setAutoCommit(false);
            System.out.println(" Transaction started");
        }
    }

    public void endTransaction() throws SQLException {
        if (connection != null) {
            connection.commit();
            connection.setAutoCommit(true);
            System.out.println(" Transaction committed");
        }
    }

    public void rollback() {
        try {
            if (connection != null) {
                connection.rollback();
                connection.setAutoCommit(true);
                System.out.println("Transaction rolled back");
            }
        } catch (SQLException e) {
            System.err.println("️ Rollback failed: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println(" Connection closed.");
            }
        } catch (SQLException e) {
            System.err.println(" Failed to close connection: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
