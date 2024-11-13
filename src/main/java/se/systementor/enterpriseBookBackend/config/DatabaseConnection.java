package se.systementor.enterpriseBookBackend.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/bookdb";
        String username = "bookServerAdmin";
        String password = "Abc123!Please";

        return DriverManager.getConnection(url, username, password);
    }
}

